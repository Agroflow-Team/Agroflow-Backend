package com.agroflow.personnel.application.service

import com.agroflow.personnel.application.port.`in`.CreateTrabajadorUseCase
import com.agroflow.personnel.application.port.`in`.FindTrabajadoresUseCase
import com.agroflow.personnel.application.port.`in`.ManageTrabajadorUseCase
import com.agroflow.personnel.application.port.`in`.TrabajadorSummaryResponse
import com.agroflow.personnel.application.port.out.TaskRepositoryPort
import com.agroflow.personnel.application.port.out.TrabajadorRepositoryPort
import com.agroflow.personnel.application.port.out.UsuarioRepositoryPort
import com.agroflow.personnel.domain.model.TaskStatus
import com.agroflow.personnel.domain.model.Trabajador
import com.agroflow.personnel.infrastructure.adapter.out.persistence.UsuarioEntity
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Service
class TrabajadorService(
    private val trabajadorRepository: TrabajadorRepositoryPort,
    private val usuarioRepository: UsuarioRepositoryPort,
    private val taskRepository: TaskRepositoryPort
) : FindTrabajadoresUseCase, CreateTrabajadorUseCase, ManageTrabajadorUseCase {

    override fun getAllTrabajadores(): List<Trabajador> {
        return trabajadorRepository.findAll()
    }

    override fun getTrabajadoresByFinca(fincaId: UUID): List<Trabajador> {
        return trabajadorRepository.findByFincaId(fincaId)
    }

    override fun createTrabajador(
        fincaId: UUID,
        nombreCompleto: String,
        documento: String,
        tarifaHora: BigDecimal,
        correo: String,
        clave: String
    ): Trabajador {
        val usuarioId = UUID.randomUUID()
        val rolTrabajadorId = com.agroflow.core.domain.Roles.TRABAJADOR

        val nuevoUsuario = UsuarioEntity(
            id = usuarioId,
            rolId = rolTrabajadorId,
            correo = correo,
            claveHash = clave,
            estado = "ACTIVO",
            fechaCreacion = LocalDateTime.now()
        )
        usuarioRepository.save(nuevoUsuario)

        val nuevoTrabajador = Trabajador(
            id = UUID.randomUUID(),
            usuarioId = usuarioId,
            fincaId = fincaId,
            nombreCompleto = nombreCompleto,
            documento = documento,
            tarifaHora = tarifaHora,
            fechaRegistro = LocalDateTime.now()
        )
        return trabajadorRepository.save(nuevoTrabajador)
    }

    override fun updateTrabajador(id: UUID, nombreCompleto: String, documento: String, tarifaHora: BigDecimal): Trabajador {
        val existente = trabajadorRepository.findById(id)
            ?: throw IllegalArgumentException("Trabajador no encontrado")
        val actualizado = existente.copy(
            nombreCompleto = nombreCompleto,
            documento = documento,
            tarifaHora = tarifaHora
        )
        return trabajadorRepository.save(actualizado)
    }

    override fun deleteTrabajador(id: UUID) {
        trabajadorRepository.deleteById(id)
    }

    override fun getTrabajadorSummary(id: UUID): TrabajadorSummaryResponse {
        val trabajador = trabajadorRepository.findById(id)
            ?: throw IllegalArgumentException("Trabajador no encontrado")
        val tareas = taskRepository.findByTrabajadorId(id)

        var horas = BigDecimal.ZERO
        var completadas = 0
        var enProgreso = 0
        var pendientes = 0

        for (tarea in tareas) {
            when (tarea.estado) {
                TaskStatus.COMPLETADA -> {
                    completadas++
                    horas = horas.add(tarea.horasInvertidas)
                }
                TaskStatus.EN_PROGRESO -> enProgreso++
                TaskStatus.PENDIENTE -> pendientes++
            }
        }

        return TrabajadorSummaryResponse(
            trabajadorId = id,
            nombreCompleto = trabajador.nombreCompleto,
            tarifaHora = trabajador.tarifaHora,
            totalHorasTrabajadas = horas,
            salarioEstimado = horas.multiply(trabajador.tarifaHora),
            tareasCompletadas = completadas,
            tareasEnProgreso = enProgreso,
            tareasPendientes = pendientes
        )
    }
}
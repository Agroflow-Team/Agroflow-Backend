package com.agroflow.personnel.infrastructure.adapter.`in`.web

import com.agroflow.personnel.application.port.`in`.CreateTrabajadorUseCase
import com.agroflow.personnel.application.port.`in`.FindTrabajadoresUseCase
import com.agroflow.personnel.application.port.`in`.ManageTrabajadorUseCase
import com.agroflow.personnel.application.port.`in`.TrabajadorSummaryResponse
import com.agroflow.personnel.domain.model.Trabajador
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.util.UUID

@RestController
@RequestMapping("/api/trabajadores")
class TrabajadorController(
    private val findTrabajadoresUseCase: FindTrabajadoresUseCase,
    private val createTrabajadorUseCase: CreateTrabajadorUseCase,
    private val manageTrabajadorUseCase: ManageTrabajadorUseCase
) {

    // Consultar todos los trabajadores
    @GetMapping
    fun getAllTrabajadores(): ResponseEntity<List<Trabajador>> {
        val trabajadores = findTrabajadoresUseCase.getAllTrabajadores()
        return ResponseEntity.ok(trabajadores)
    }

    // Consultar trabajadores filtrados por finca (¡Súper útil para la app móvil!)
    @GetMapping("/finca/{fincaId}")
    fun getTrabajadoresByFinca(@PathVariable fincaId: UUID): ResponseEntity<List<Trabajador>> {
        val trabajadores = findTrabajadoresUseCase.getTrabajadoresByFinca(fincaId)
        return ResponseEntity.ok(trabajadores)
    }

    @PostMapping
    fun createTrabajador(@RequestBody request: CreateTrabajadorRequest): ResponseEntity<Trabajador> {
        val trabajador = createTrabajadorUseCase.createTrabajador(
            fincaId = request.fincaId,
            nombreCompleto = request.nombreCompleto,
            documento = request.documento,
            tarifaHora = request.tarifaHora,
            correo = request.correo,
            clave = request.clave
        )
        return ResponseEntity.ok(trabajador)
    }

    @PutMapping("/{id}")
    fun updateTrabajador(@PathVariable id: UUID, @RequestBody request: UpdateTrabajadorRequest): ResponseEntity<Trabajador> {
        val trabajador = manageTrabajadorUseCase.updateTrabajador(
            id = id,
            nombreCompleto = request.nombreCompleto,
            documento = request.documento,
            tarifaHora = request.tarifaHora
        )
        return ResponseEntity.ok(trabajador)
    }

    @DeleteMapping("/{id}")
    fun deleteTrabajador(@PathVariable id: UUID): ResponseEntity<Void> {
        manageTrabajadorUseCase.deleteTrabajador(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/{id}/summary")
    fun getTrabajadorSummary(@PathVariable id: UUID): ResponseEntity<TrabajadorSummaryResponse> {
        val summary = manageTrabajadorUseCase.getTrabajadorSummary(id)
        return ResponseEntity.ok(summary)
    }
}

data class CreateTrabajadorRequest(
    val fincaId: UUID,
    val nombreCompleto: String,
    val documento: String,
    val tarifaHora: BigDecimal,
    val correo: String,
    val clave: String
)

data class UpdateTrabajadorRequest(
    val nombreCompleto: String,
    val documento: String,
    val tarifaHora: BigDecimal
)
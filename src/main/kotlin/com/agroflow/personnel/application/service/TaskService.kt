package com.agroflow.personnel.application.service

import com.agroflow.personnel.application.port.`in`.ManageTaskUseCase
import com.agroflow.personnel.application.port.out.TaskRepositoryPort
import com.agroflow.personnel.application.port.out.TrabajadorRepositoryPort
import com.agroflow.personnel.application.port.out.UsuarioRepositoryPort
import com.agroflow.personnel.application.port.out.FincaRepositoryPort
import com.agroflow.personnel.domain.model.Task
import com.agroflow.personnel.domain.model.TaskStatus
import com.agroflow.personnel.application.service.NotificationService
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.UUID

@Service
class TaskService(
    private val taskRepository: TaskRepositoryPort,
    private val trabajadorRepository: TrabajadorRepositoryPort,
    private val usuarioRepository: UsuarioRepositoryPort,
    private val notificationService: NotificationService
) : ManageTaskUseCase {

    override fun getTasksByWorker(trabajadorId: UUID): List<Task> {
        return taskRepository.findByTrabajadorId(trabajadorId)
    }

    override fun getTasksByFinca(fincaId: UUID): List<Task> {
        return taskRepository.findByFincaId(fincaId)
    }

    override fun updateTaskProgress(
        taskId: UUID,
        trabajadorId: UUID,
        nuevasHoras: BigDecimal,
        novedades: String,
        nuevoEstado: TaskStatus,
        severidadNovedad: String?
    ): Task {
        val task = taskRepository.findById(taskId)
            ?: throw IllegalArgumentException("La tarea no existe")

        if (task.trabajadorId != trabajadorId) {
            throw SecurityException("No tienes permiso para modificar esta tarea")
        }

        task.reportarAvance(nuevasHoras, novedades, nuevoEstado, severidadNovedad)
        val savedTask = taskRepository.save(task)

        // Enviar notificacion al agricultor
        try {
            val trabajador = trabajadorRepository.findById(trabajadorId)
            
            if (trabajador != null) {
                // Notificar a todos los admins
                val admins = usuarioRepository.findByRolId(com.agroflow.core.domain.Roles.ADMIN)
                val tokens = admins.mapNotNull { it.fcmToken }.filter { it.isNotBlank() }
                
                if (tokens.isNotEmpty()) {
                    val titulo = if (nuevoEstado == TaskStatus.COMPLETADA) {
                        "Tarea Completada \uD83D\uDFE2" // Verde
                    } else {
                        "Avance de Tarea \uD83D\uDD35" // Azul
                    }
                    
                    val cuerpo = if (nuevoEstado == TaskStatus.COMPLETADA) {
                        "${trabajador.nombreCompleto} ha finalizado la tarea '${task.titulo}'. Recuerda registrar su pago."
                    } else {
                        "${trabajador.nombreCompleto} movió la tarea '${task.titulo}' a ${nuevoEstado.name}."
                    }
                    
                    tokens.forEach { token ->
                        notificationService.sendPushNotification(token, titulo, cuerpo)
                    }
                }
            }
        } catch (e: Exception) {
            System.err.println("Error enviando notificacion de avance: \${e.message}")
        }

        return savedTask
    }
}
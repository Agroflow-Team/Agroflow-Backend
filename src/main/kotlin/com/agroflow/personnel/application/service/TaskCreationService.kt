package com.agroflow.personnel.application.service

import com.agroflow.personnel.application.port.`in`.CreateTaskUseCase
import com.agroflow.personnel.application.port.out.TaskRepositoryPort
import com.agroflow.personnel.domain.model.Task
import org.springframework.stereotype.Service

import com.agroflow.personnel.application.port.out.TrabajadorRepositoryPort
import com.agroflow.personnel.infrastructure.adapter.out.persistence.SpringDataUsuarioRepository

@Service
class TaskCreationService(
    private val taskRepository: TaskRepositoryPort,
    private val trabajadorRepository: TrabajadorRepositoryPort,
    private val usuarioRepository: SpringDataUsuarioRepository,
    private val notificationService: NotificationService
) : CreateTaskUseCase {

    override fun createTask(task: Task): Task {
        val savedTask = taskRepository.save(task)
        
        try {
            println("TaskCreationService: Procesando notificación para tarea '${task.titulo}' con trabajadorId=${task.trabajadorId}")
            
            // 1. Buscar si trabajadorId es el ID de perfil o el ID de usuario
            val trabajador = trabajadorRepository.findById(task.trabajadorId)
                ?: trabajadorRepository.findAll().find { it.usuarioId == task.trabajadorId }
            
            val usuarioId = trabajador?.usuarioId ?: task.trabajadorId
            var usuarioEntity = usuarioRepository.findById(usuarioId).orElse(null)
            
            if (usuarioEntity == null && trabajador != null) {
                usuarioEntity = usuarioRepository.findAll().find { 
                    it.id == trabajador.usuarioId || it.correo.equals(trabajador.nombreCompleto, ignoreCase = true) 
                }
            }
            
            val token = usuarioEntity?.fcmToken
            println("TaskCreationService: Usuario destinatario: ${usuarioEntity?.correo ?: "No encontrado"}, Token FCM: ${if (token.isNullOrBlank()) "NO DISPONIBLE / VACIO" else "PRESENTE (${token.take(12)}...)"}")
            
            if (!token.isNullOrBlank()) {
                notificationService.sendPushNotification(
                    token = token,
                    title = "¡Nueva tarea asignada!",
                    body = "Tienes una nueva tarea: ${task.titulo}"
                )
                println("TaskCreationService: Notificación push enviada con éxito para ${usuarioEntity?.correo}")
            } else {
                println("TaskCreationService: No se pudo enviar notificación push porque el usuario no tiene token FCM registrado en BD.")
            }
        } catch (e: Exception) {
            println("TaskCreationService: Error al enviar notificación push: ${e.message}")
            e.printStackTrace()
        }
        
        return savedTask
    }
}
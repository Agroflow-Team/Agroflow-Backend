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
            println("[TaskCreationService] Intentando enviar notificación para la tarea: ${task.titulo}, trabajadorId: ${task.trabajadorId}")
            
            var token: String? = null
            
            // 1. Intentar buscar por ID de perfil de trabajador
            val trabajador = trabajadorRepository.findById(task.trabajadorId)
            if (trabajador != null) {
                val usuarioEntity = usuarioRepository.findById(trabajador.usuarioId).orElse(null)
                token = usuarioEntity?.fcmToken
            }
            
            // 2. Si no se encontró el token, intentar buscar directamente como usuarioId
            if (token.isNullOrBlank()) {
                val usuarioDirecto = usuarioRepository.findById(task.trabajadorId).orElse(null)
                token = usuarioDirecto?.fcmToken
            }
            
            if (!token.isNullOrBlank()) {
                println("[TaskCreationService] Token FCM encontrado. Enviando push...")
                notificationService.sendPushNotification(
                    token = token,
                    title = "¡Nueva tarea asignada!",
                    body = "Tienes una nueva tarea: ${task.titulo}"
                )
            } else {
                println("[TaskCreationService] WARN: No se encontró token FCM para el trabajador ID: ${task.trabajadorId}")
            }
        } catch (e: Exception) {
            println("[TaskCreationService] ERROR al procesar notificación: ${e.message}")
            e.printStackTrace()
        }
        
        return savedTask
    }
}
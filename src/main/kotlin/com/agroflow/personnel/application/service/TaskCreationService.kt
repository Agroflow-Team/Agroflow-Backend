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
            val trabajador = trabajadorRepository.findById(task.trabajadorId)
            if (trabajador != null) {
                val usuarioEntity = usuarioRepository.findById(trabajador.usuarioId).orElse(null)
                val token = usuarioEntity?.fcmToken
                
                if (token != null && token.isNotBlank()) {
                    notificationService.sendPushNotification(
                        token = token,
                        title = "¡Nueva tarea asignada!",
                        body = "Tienes una nueva tarea: ${task.titulo}"
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        return savedTask
    }
}
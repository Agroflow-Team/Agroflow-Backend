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
                println("[TaskCreationService] Perfil de trabajador encontrado: ${trabajador.nombreCompleto}, usuarioId: ${trabajador.usuarioId}")
                val usuarioEntity = usuarioRepository.findById(trabajador.usuarioId).orElse(null)
                if (usuarioEntity != null) {
                    token = usuarioEntity.fcmToken
                    println("[TaskCreationService] Usuario encontrado: ${usuarioEntity.correo}, fcmToken: ${if (token.isNullOrBlank()) "VACÍO" else "${token.take(15)}..."}")
                } else {
                    println("[TaskCreationService] WARN: No se encontró usuario con ID: ${trabajador.usuarioId}")
                }
            } else {
                println("[TaskCreationService] WARN: No se encontró perfil de trabajador con ID: ${task.trabajadorId}")
            }
            
            // 2. Si no se encontró el token, intentar buscar directamente como usuarioId
            if (token.isNullOrBlank()) {
                println("[TaskCreationService] Intentando buscar directamente como usuarioId...")
                val usuarioDirecto = usuarioRepository.findById(task.trabajadorId).orElse(null)
                if (usuarioDirecto != null) {
                    token = usuarioDirecto.fcmToken
                    println("[TaskCreationService] Usuario directo encontrado: ${usuarioDirecto.correo}, fcmToken: ${if (token.isNullOrBlank()) "VACÍO" else "${token.take(15)}..."}")
                } else {
                    println("[TaskCreationService] WARN: Tampoco se encontró usuario directo con ID: ${task.trabajadorId}")
                }
            }
            
            if (!token.isNullOrBlank()) {
                println("[TaskCreationService] Token FCM encontrado. Enviando push...")
                notificationService.sendPushNotification(
                    token = token,
                    title = "¡Nueva tarea asignada!",
                    body = "Tienes una nueva tarea: ${task.titulo}"
                )
            } else {
                System.err.println("[TaskCreationService] ERROR: No se encontró token FCM para el trabajador ID: ${task.trabajadorId}. El trabajador debe iniciar sesión en la app para registrar su token.")
            }
        } catch (e: Exception) {
            System.err.println("[TaskCreationService] ERROR al procesar notificación: ${e.message}")
            e.printStackTrace()
        }
        
        return savedTask
    }
}
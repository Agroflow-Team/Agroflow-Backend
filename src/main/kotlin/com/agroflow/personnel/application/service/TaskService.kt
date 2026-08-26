package com.agroflow.personnel.application.service

import com.agroflow.personnel.application.port.`in`.ManageTaskUseCase
import com.agroflow.personnel.application.port.out.TaskRepositoryPort
import com.agroflow.personnel.domain.model.Task
import com.agroflow.personnel.domain.model.TaskStatus
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.UUID

@Service
class TaskService(
    private val taskRepository: TaskRepositoryPort
) : ManageTaskUseCase {

    override fun getTasksByWorker(trabajadorId: UUID): List<Task> {
        return taskRepository.findByTrabajadorId(trabajadorId)
    }

    override fun updateTaskProgress(
        taskId: UUID,
        trabajadorId: UUID,
        nuevasHoras: BigDecimal,
        novedades: String,
        nuevoEstado: TaskStatus
    ): Task {
        // 1. Buscamos la tarea en la base de datos
        val task = taskRepository.findById(taskId)
            ?: throw IllegalArgumentException("La tarea no existe")

        // 2. Regla de negocio vital: Validar que la tarea pertenezca al trabajador que intenta modificarla
        if (task.trabajadorId != trabajadorId) {
            throw SecurityException("No tienes permiso para modificar esta tarea")
        }

        // 3. Actualizamos usando la funcion pura de nuestro Dominio
        task.reportarAvance(nuevasHoras, novedades, nuevoEstado)

        // 4. Guardamos los cambios
        return taskRepository.save(task)
    }
}
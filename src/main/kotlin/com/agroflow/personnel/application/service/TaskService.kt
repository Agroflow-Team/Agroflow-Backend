package com.agroflow.personnel.application.service

import com.agroflow.personnel.application.port.`in`.ManageTaskUseCase
import com.agroflow.personnel.application.port.out.TaskRepositoryPort
import com.agroflow.personnel.application.port.out.TrabajadorRepositoryPort
import com.agroflow.personnel.domain.model.Task
import com.agroflow.personnel.domain.model.TaskStatus
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.UUID

@Service
class TaskService(
    private val taskRepository: TaskRepositoryPort,
    private val trabajadorRepository: TrabajadorRepositoryPort
) : ManageTaskUseCase {

    override fun getTasksByWorker(trabajadorId: UUID): List<Task> {
        val tasks = taskRepository.findByTrabajadorId(trabajadorId)
        if (tasks.isNotEmpty()) return tasks

        // Si no encontró tareas, puede que trabajadorId sea el usuarioId
        val trabajador = trabajadorRepository.findAll().find { it.usuarioId == trabajadorId }
        if (trabajador?.id != null) {
            return taskRepository.findByTrabajadorId(trabajador.id!!)
        }
        return tasks
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
        // 1. Buscamos la tarea en la base de datos
        val task = taskRepository.findById(taskId)
            ?: throw IllegalArgumentException("La tarea no existe")

        // 2. Validar que la tarea pertenezca al trabajador (comprobando tanto ID de perfil como ID de usuario)
        val trabajador = trabajadorRepository.findById(trabajadorId) 
            ?: trabajadorRepository.findAll().find { it.usuarioId == trabajadorId }
        
        val taskTrabajador = trabajadorRepository.findById(task.trabajadorId)

        val isOwner = task.trabajadorId == trabajadorId ||
                      trabajador?.id == task.trabajadorId ||
                      trabajador?.usuarioId == task.trabajadorId ||
                      (taskTrabajador != null && (taskTrabajador.usuarioId == trabajadorId || taskTrabajador.id == trabajadorId))

        if (!isOwner) {
            throw SecurityException("No tienes permiso para modificar esta tarea")
        }

        // 3. Actualizamos usando la funcion pura de nuestro Dominio
        task.reportarAvance(nuevasHoras, novedades, nuevoEstado, severidadNovedad)

        // 4. Guardamos los cambios
        return taskRepository.save(task)
    }
}
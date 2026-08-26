package com.agroflow.personnel.application.service

import com.agroflow.personnel.application.port.`in`.CreateTaskUseCase
import com.agroflow.personnel.application.port.out.TaskRepositoryPort
import com.agroflow.personnel.domain.model.Task
import org.springframework.stereotype.Service

@Service
class TaskCreationService(
    private val taskRepository: TaskRepositoryPort
) : CreateTaskUseCase {

    override fun createTask(task: Task): Task {
        // Aqui puedes agregar reglas de negocio en el futuro (ej: validar que el titulo no este vacio)
        return taskRepository.save(task)
    }
}
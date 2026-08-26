package com.agroflow.personnel.infrastructure.adapter.out.persistence

import com.agroflow.personnel.application.port.out.TaskRepositoryPort
import com.agroflow.personnel.domain.model.Task
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class TaskRepositoryAdapter(
    private val repository: SpringDataTaskRepository
) : TaskRepositoryPort {

    override fun save(task: Task): Task {
        val entity = TaskEntity.fromDomain(task)
        val savedEntity = repository.save(entity)
        return savedEntity.toDomain()
    }

    override fun findById(id: UUID): Task? {
        return repository.findById(id).map { it.toDomain() }.orElse(null)
    }

    override fun findByTrabajadorId(trabajadorId: UUID): List<Task> {
        return repository.findByTrabajadorId(trabajadorId).map { it.toDomain() }
    }
}
package com.agroflow.personnel.application.port.out

import com.agroflow.personnel.domain.model.Task
import java.util.UUID

interface TaskRepositoryPort {
    fun save(task: Task): Task
    fun findById(id: UUID): Task?
    fun findByTrabajadorId(trabajadorId: UUID): List<Task>
    fun findByFincaId(fincaId: UUID): List<Task>
}
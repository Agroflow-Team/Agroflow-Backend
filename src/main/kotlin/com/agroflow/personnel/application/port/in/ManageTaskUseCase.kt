package com.agroflow.personnel.application.port.`in`

import com.agroflow.personnel.domain.model.Task
import com.agroflow.personnel.domain.model.TaskStatus
import java.math.BigDecimal
import java.util.UUID

interface ManageTaskUseCase {
    fun getTasksByWorker(trabajadorId: UUID): List<Task>
    fun updateTaskProgress(taskId: UUID, trabajadorId: UUID, nuevasHoras: BigDecimal, novedades: String, nuevoEstado: TaskStatus): Task
}
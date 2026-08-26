package com.agroflow.personnel.application.port.`in`

import com.agroflow.personnel.domain.model.Task

interface CreateTaskUseCase {
    fun createTask(task: Task): Task
}
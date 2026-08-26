package com.agroflow.personnel.infrastructure.adapter.`in`.web

import com.agroflow.personnel.application.port.`in`.CreateTaskUseCase // <-- Nuevo import
import com.agroflow.personnel.application.port.`in`.ManageTaskUseCase
import com.agroflow.personnel.domain.model.Task
import com.agroflow.personnel.domain.model.TaskStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.util.UUID

@RestController
@RequestMapping("/api/tasks")
class TaskController(
    private val manageTaskUseCase: ManageTaskUseCase,
    private val createTaskUseCase: CreateTaskUseCase // <-- Inyectamos el nuevo caso de uso
) {

    // Endpoint nuevo: Crear una tarea desde cero (POST /api/tasks)
    @PostMapping
    fun createTask(@RequestBody request: CreateTaskRequest): ResponseEntity<Task> {
        val newTask = Task(
            fincaId = request.fincaId,
            trabajadorId = request.trabajadorId,
            loteId = request.loteId,
            titulo = request.titulo,
            descripcion = request.descripcion,
            estado = request.estado ?: TaskStatus.PENDIENTE
        )
        val created = createTaskUseCase.createTask(newTask)
        return ResponseEntity.ok(created)
    }

    @GetMapping("/worker/{trabajadorId}")
    fun getTasks(@PathVariable trabajadorId: UUID): ResponseEntity<List<Task>> {
        val tasks = manageTaskUseCase.getTasksByWorker(trabajadorId)
        return ResponseEntity.ok(tasks)
    }

    @PatchMapping("/{taskId}/progress")
    fun updateProgress(
        @PathVariable taskId: UUID,
        @RequestBody request: UpdateProgressRequest
    ): ResponseEntity<Task> {
        val updatedTask = manageTaskUseCase.updateTaskProgress(
            taskId,
            request.trabajadorId,
            request.nuevasHoras,
            request.novedades,
            request.nuevoEstado
        )
        return ResponseEntity.ok(updatedTask)
    }
}

// DTO para recibir los datos de creacion
data class CreateTaskRequest(
    val fincaId: UUID,
    val trabajadorId: UUID,
    val loteId: UUID?,
    val titulo: String,
    val descripcion: String,
    val estado: TaskStatus?
)

data class UpdateProgressRequest(
    val trabajadorId: UUID,
    val nuevasHoras: BigDecimal,
    val novedades: String,
    val nuevoEstado: TaskStatus
)
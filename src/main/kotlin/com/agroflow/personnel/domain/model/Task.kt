package com.agroflow.personnel.domain.model

import java.util.UUID
import java.time.LocalDateTime
import java.math.BigDecimal

class Task(
    val id: UUID? = null,
    val fincaId: UUID,
    val trabajadorId: UUID,
    val loteId: UUID? = null, // Puede ser nulo si la tarea no es de un lote especifico
    var titulo: String,
    var descripcion: String,
    var estado: TaskStatus,
    var horasInvertidas: BigDecimal = BigDecimal.ZERO,
    var novedades: String? = null,
    var fechaActualizacion: LocalDateTime = LocalDateTime.now(),
    var eliminado: Boolean = false,
    var estadoSincronizacion: String = "PENDIENTE"
) {
    // Regla de negocio para el trabajador
    fun reportarAvance(nuevasHoras: BigDecimal, nuevaNovedad: String, nuevoEstado: TaskStatus) {
        this.horasInvertidas = this.horasInvertidas.add(nuevasHoras)
        this.novedades = nuevaNovedad
        this.estado = nuevoEstado
        this.fechaActualizacion = LocalDateTime.now()
        this.estadoSincronizacion = "MODIFICADO"
    }
}

// Enum basado en tu campo estado_tarea_enum
enum class TaskStatus {
    PENDIENTE, EN_PROGRESO, COMPLETADA
}
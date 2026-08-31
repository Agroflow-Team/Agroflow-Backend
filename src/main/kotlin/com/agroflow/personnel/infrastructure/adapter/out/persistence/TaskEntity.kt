package com.agroflow.personnel.infrastructure.adapter.out.persistence

import com.agroflow.personnel.domain.model.Task
import com.agroflow.personnel.domain.model.TaskStatus
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "tareas")
class TaskEntity(
    @Id
    @Column(name = "id")
    val id: UUID? = null,

    @Column(name = "finca_id")
    val fincaId: UUID,

    @Column(name = "trabajador_id")
    val trabajadorId: UUID,

    @Column(name = "lote_id")
    val loteId: UUID?,

    @Column(name = "titulo")
    var titulo: String,

    @Column(name = "descripcion")
    var descripcion: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    var estado: TaskStatus,

    @Column(name = "horas_invertidas")
    var horasInvertidas: BigDecimal,

    @Column(name = "novedades")
    var novedades: String?,

    @Column(name = "fecha_actualizacion")
    var fechaActualizacion: LocalDateTime,

    @Column(name = "eliminado")
    var eliminado: Boolean,

    @Column(name = "estado_sincronizacion")
    var estadoSincronizacion: String,

    @Column(name = "severidad_novedad")
    val severidadNovedad: String? = null
) {
    // Funciones para convertir entre la Base de Datos (Entity) y el Dominio (Task)
    fun toDomain(): Task = Task(
        id = id,
        fincaId = fincaId,
        trabajadorId = trabajadorId,
        loteId = loteId,
        titulo = titulo,
        descripcion = descripcion,
        estado = estado,
        horasInvertidas = horasInvertidas,
        novedades = novedades,
        fechaActualizacion = fechaActualizacion,
        eliminado = eliminado,
        estadoSincronizacion = estadoSincronizacion,
        severidadNovedad = severidadNovedad
    )

    companion object {
        fun fromDomain(task: Task): TaskEntity = TaskEntity(
            id = task.id ?: UUID.randomUUID(),
            fincaId = task.fincaId,
            trabajadorId = task.trabajadorId,
            loteId = task.loteId,
            titulo = task.titulo,
            descripcion = task.descripcion,
            estado = task.estado,
            horasInvertidas = task.horasInvertidas,
            novedades = task.novedades,
            fechaActualizacion = task.fechaActualizacion,
            eliminado = task.eliminado,
            estadoSincronizacion = task.estadoSincronizacion,
            severidadNovedad = task.severidadNovedad
        )
    }
}
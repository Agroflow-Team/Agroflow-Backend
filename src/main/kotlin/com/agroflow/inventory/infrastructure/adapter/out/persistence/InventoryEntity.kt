package com.agroflow.inventory.infrastructure.adapter.out.persistence

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "inventario")
data class InventoryEntity(
    @Id
    val id: UUID? = null,

    @Column(name = "finca_id", nullable = false)
    val fincaId: UUID,

    @Column(name = "registrado_por_trabajador_id", nullable = false)
    val registradoPorTrabajadorId: UUID,

    @Column(name = "nombre_item", nullable = false)
    val nombreItem: String,

    @Column(name = "tipo", nullable = false)
    val tipo: String,

    @Column(name = "cantidad", nullable = false)
    val cantidad: BigDecimal,

    @Column(name = "unidad_medida", nullable = false)
    val unidadMedida: String,

    @Column(name = "fecha_actualizacion")
    val fechaActualizacion: LocalDateTime?,

    @Column(name = "eliminado", nullable = false)
    val eliminado: Boolean,

    @Column(name = "costo_unitario")
    val costoUnitario: BigDecimal?,

    @Column(name = "estado_sincronizacion", nullable = false)
    val estadoSincronizacion: String
)
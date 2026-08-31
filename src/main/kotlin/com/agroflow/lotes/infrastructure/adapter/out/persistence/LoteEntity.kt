package com.agroflow.lotes.infrastructure.adapter.out.persistence

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "lotes")
data class LoteEntity(
    @Id
    val id: UUID? = null,

    @Column(name = "finca_id", nullable = false)
    val fincaId: UUID,

    @Column(name = "nombre", nullable = false)
    val nombre: String,

    @Column(name = "latitud")
    val latitud: Double?,

    @Column(name = "longitud")
    val longitud: Double?,

    @Column(name = "fecha_registro", nullable = false)
    val fechaRegistro: LocalDateTime,

    @Column(name = "estado_sincronizacion", nullable = false)
    val estadoSincronizacion: String
)

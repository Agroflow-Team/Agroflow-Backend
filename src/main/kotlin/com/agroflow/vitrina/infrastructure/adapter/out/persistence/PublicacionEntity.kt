package com.agroflow.vitrina.infrastructure.adapter.out.persistence

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "publicaciones")
data class PublicacionEntity(
    @Id
    val id: UUID? = null,

    @Column(name = "finca_id", nullable = false)
    val fincaId: UUID,

    @Column(name = "titulo_producto", nullable = false)
    val tituloProducto: String,

    @Column(name = "descripcion", nullable = false)
    val descripcion: String,

    @Column(name = "precio", nullable = false)
    val precio: BigDecimal,

    @Column(name = "cantidad_disponible", nullable = false)
    val cantidadDisponible: Int,

    @Column(name = "imagen_url")
    val imagenUrl: String?,

    @Column(name = "estado_publicacion", nullable = false)
    val estadoPublicacion: String,

    @Column(name = "fecha_creacion", nullable = false)
    val fechaCreacion: LocalDateTime,

    @Column(name = "estado_sincronizacion", nullable = false)
    val estadoSincronizacion: String
)

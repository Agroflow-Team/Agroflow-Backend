package com.agroflow.vitrina.domain.model

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

enum class EstadoPublicacion {
    ACTIVA, VENDIDA, INACTIVA
}

data class Publicacion(
    val id: UUID? = null,
    val fincaId: UUID,
    val tituloProducto: String,
    val descripcion: String,
    val precio: BigDecimal,
    val cantidadDisponible: Int,
    val imagenUrl: String?,
    val estadoPublicacion: EstadoPublicacion,
    val fechaCreacion: LocalDateTime,
    val estadoSincronizacion: String = "SINCRONIZADO"
)

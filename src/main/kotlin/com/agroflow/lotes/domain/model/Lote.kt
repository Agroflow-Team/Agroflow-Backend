package com.agroflow.lotes.domain.model

import java.time.LocalDateTime
import java.util.UUID

data class Lote(
    val id: UUID? = null,
    val fincaId: UUID,
    val nombre: String,
    val latitud: Double?,
    val longitud: Double?,
    val fechaRegistro: LocalDateTime,
    val estadoSincronizacion: String = "SINCRONIZADO"
)

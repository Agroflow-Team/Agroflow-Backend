package com.agroflow.personnel.domain.model

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

data class Trabajador(
    val id: UUID? = null,
    val usuarioId: UUID,
    val fincaId: UUID,
    val nombreCompleto: String,
    val documento: String,
    val tarifaHora: BigDecimal,
    val fechaRegistro: LocalDateTime = LocalDateTime.now()
)
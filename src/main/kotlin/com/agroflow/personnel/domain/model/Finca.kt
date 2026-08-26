package com.agroflow.personnel.domain.model

import java.time.LocalDateTime
import java.util.UUID

data class Finca(
    val id: UUID? = null,
    val nombre: String,
    val fechaRegistro: LocalDateTime = LocalDateTime.now()
)
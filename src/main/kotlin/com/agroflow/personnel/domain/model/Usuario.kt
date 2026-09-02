package com.agroflow.personnel.domain.model

import java.util.UUID

data class Usuario(
    val id: UUID? = null,
    val rolId: UUID,
    val correo: String,
    val estado: String,
    var fcmToken: String? = null
)
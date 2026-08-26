package com.agroflow.personnel.application.port.`in`

import java.util.UUID

data class LoginResponse(
    val usuarioId: UUID,
    val correo: String,
    val rolId: UUID,
    val token: String // Token simple o mensaje de éxito para la app
)
package com.agroflow.identity.domain.model

import java.util.UUID

class User(
    val id: UUID? = null,
    val correo: String,
    val claveHash: String,
    val rol: Role,
    val estado: String
)
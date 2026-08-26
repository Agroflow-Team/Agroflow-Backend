package com.agroflow.personnel.application.port.`in`

data class LoginRequest(
    val correo: String,
    val clave: String // Contraseña ingresada en la app
)

interface LoginUseCase {
    fun authenticate(request: LoginRequest): LoginResponse
}
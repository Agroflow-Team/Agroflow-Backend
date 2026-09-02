package com.agroflow.auth.infrastructure.adapter.`in`.web

import com.agroflow.auth.application.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class LoginRequest(
    val correo: String,
    val clave: String,
    val fcmToken: String? = null
)

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<Any> {
        return try {
            val response = authService.login(request.correo, request.clave, request.fcmToken)
            ResponseEntity.ok(response)
        } catch (e: Exception) {
            ResponseEntity.status(401).body(mapOf("error" to (e.message ?: "Unauthorized")))
        }
    }
}

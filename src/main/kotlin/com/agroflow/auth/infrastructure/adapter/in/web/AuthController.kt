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

    @PostMapping("/fcm-token")
    fun updateFcmToken(@RequestBody request: UpdateFcmTokenRequest): ResponseEntity<Any> {
        return try {
            authService.updateFcmToken(request.usuarioId, request.fcmToken)
            ResponseEntity.ok(mapOf("message" to "FCM token updated successfully"))
        } catch (e: Exception) {
            ResponseEntity.status(400).body(mapOf("error" to (e.message ?: "Bad Request")))
        }
    }

    @org.springframework.web.bind.annotation.GetMapping("/test-firebase")
    fun checkFirebase(): ResponseEntity<Map<String, Any>> {
        val apps = com.google.firebase.FirebaseApp.getApps()
        val envRaw = System.getenv("FIREBASE_CREDENTIALS")
        var initError = "No intentado"
        if (apps.isEmpty() && !envRaw.isNullOrBlank()) {
            try {
                val stream = java.io.ByteArrayInputStream(envRaw.toByteArray(Charsets.UTF_8))
                val options = com.google.firebase.FirebaseOptions.builder()
                    .setCredentials(com.google.auth.oauth2.GoogleCredentials.fromStream(stream))
                    .build()
                com.google.firebase.FirebaseApp.initializeApp(options)
                initError = "Inicialización exitosa ahora mismo"
            } catch (e: Exception) {
                initError = e.message ?: "Excepción sin mensaje: ${e.javaClass.name}"
            }
        }
        return ResponseEntity.ok(mapOf(
            "firebaseConfigured" to com.google.firebase.FirebaseApp.getApps().isNotEmpty(),
            "hasEnvVar" to !envRaw.isNullOrBlank(),
            "envVarLength" to (envRaw?.length ?: 0),
            "initError" to initError,
            "apps" to com.google.firebase.FirebaseApp.getApps().map { it.name }
        ))
    }
}

data class UpdateFcmTokenRequest(
    val usuarioId: String,
    val fcmToken: String
)

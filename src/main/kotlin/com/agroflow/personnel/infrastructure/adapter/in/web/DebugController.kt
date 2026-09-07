package com.agroflow.personnel.infrastructure.adapter.`in`.web

import com.agroflow.personnel.application.service.NotificationService

import com.agroflow.personnel.infrastructure.adapter.out.persistence.SpringDataUsuarioRepository

import org.springframework.http.ResponseEntity

import org.springframework.web.bind.annotation.*



@RestController

@RequestMapping("/api/debug")

class DebugController(

    private val usuarioRepository: SpringDataUsuarioRepository,

    private val notificationService: NotificationService

) {



    @GetMapping("/firebase")

    fun checkFirebase(): ResponseEntity<Map<String, Any>> {

        val apps = com.google.firebase.FirebaseApp.getApps()

        return ResponseEntity.ok(mapOf(

            "firebaseConfigured" to apps.isNotEmpty(),

            "apps" to apps.map { it.name }

        ))

    }



    @GetMapping("/users")

    fun checkUserTokens(): ResponseEntity<List<Map<String, String?>>> {

        val users = usuarioRepository.findAll()

        return ResponseEntity.ok(users.map {

            mapOf(

                "correo" to it.correo,

                "fcmToken" to (if (it.fcmToken.isNullOrBlank()) "Vacio" else "Configurado (empieza con ${it.fcmToken?.take(10)}...)")

            )

        })

    }



    @PostMapping("/test-push")

    fun testPush(@RequestParam correo: String): ResponseEntity<String> {

        val user = usuarioRepository.findByCorreo(correo).orElse(null)

            ?: return ResponseEntity.badRequest().body("Usuario no encontrado")


        val token = user.fcmToken

        if (token.isNullOrBlank()) return ResponseEntity.badRequest().body("Usuario no tiene FCM token")


        try {

            notificationService.sendPushNotification(token, "Test Push", "Esto es una prueba manual")

            return ResponseEntity.ok("Enviado. Revisa los logs de Render para detalles.")

        } catch(e: Exception) {

            return ResponseEntity.status(500).body("Error enviando: ${e.message}")

        }

    }

}
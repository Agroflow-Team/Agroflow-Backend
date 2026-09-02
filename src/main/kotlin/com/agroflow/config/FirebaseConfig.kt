package com.agroflow.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.context.annotation.Configuration
import java.io.ByteArrayInputStream
import javax.annotation.PostConstruct

@Configuration
class FirebaseConfig {

    @PostConstruct
    fun initialize() {
        try {
            val envCredentials = System.getenv("FIREBASE_CREDENTIALS")
            val credentialsStream = if (!envCredentials.isNullOrBlank()) {
                println("Cargando Firebase desde Variable de Entorno (FIREBASE_CREDENTIALS)")
                ByteArrayInputStream(envCredentials.toByteArray(Charsets.UTF_8))
            } else {
                println("Cargando Firebase desde archivo local firebase-admin.json")
                javaClass.classLoader.getResourceAsStream("firebase-admin.json")
            }

            if (credentialsStream != null) {
                val options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(credentialsStream))
                    .build()
                
                if (FirebaseApp.getApps().isEmpty()) {
                    FirebaseApp.initializeApp(options)
                }
            } else {
                println("No se encontraron credenciales de Firebase. Notificaciones deshabilitadas.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

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
                println("[FirebaseConfig] Cargando Firebase desde Variable de Entorno (FIREBASE_CREDENTIALS) - ${envCredentials.length} caracteres")
                ByteArrayInputStream(envCredentials.toByteArray(Charsets.UTF_8))
            } else {
                println("[FirebaseConfig] Variable FIREBASE_CREDENTIALS no encontrada. Intentando archivo local firebase-admin.json...")
                val stream = javaClass.classLoader.getResourceAsStream("firebase-admin.json")
                if (stream != null) {
                    println("[FirebaseConfig] Archivo firebase-admin.json encontrado en resources.")
                } else {
                    System.err.println("[FirebaseConfig] ERROR: No se encontró firebase-admin.json en resources.")
                }
                stream
            }

            if (credentialsStream != null) {
                val options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(credentialsStream))
                    .build()
                
                if (FirebaseApp.getApps().isEmpty()) {
                    FirebaseApp.initializeApp(options)
                    println("[FirebaseConfig] ✅ Firebase inicializado correctamente. Notificaciones push habilitadas.")
                } else {
                    println("[FirebaseConfig] Firebase ya estaba inicializado.")
                }
            } else {
                System.err.println("[FirebaseConfig] ERROR: No se encontraron credenciales de Firebase. Las notificaciones push NO funcionarán.")
                System.err.println("[FirebaseConfig] Configura la variable de entorno FIREBASE_CREDENTIALS o coloca firebase-admin.json en src/main/resources/")
            }
        } catch (e: Exception) {
            System.err.println("[FirebaseConfig] ERROR al inicializar Firebase: ${e.message}")
            e.printStackTrace()
        }
    }
}

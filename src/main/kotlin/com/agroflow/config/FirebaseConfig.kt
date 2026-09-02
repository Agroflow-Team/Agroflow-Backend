package com.agroflow.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct

@Configuration
class FirebaseConfig {

    @PostConstruct
    fun initialize() {
        try {
            val serviceAccount = javaClass.classLoader.getResourceAsStream("firebase-admin.json")
            if (serviceAccount != null) {
                val options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build()
                
                if (FirebaseApp.getApps().isEmpty()) {
                    FirebaseApp.initializeApp(options)
                }
            } else {
                println("No se encontro firebase-admin.json en resources.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

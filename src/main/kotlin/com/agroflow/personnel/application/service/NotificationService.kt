package com.agroflow.personnel.application.service

import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingException
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import org.springframework.stereotype.Service

@Service
class NotificationService {

    fun sendPushNotification(token: String, title: String, body: String) {
        // Verificar que Firebase esté inicializado
        if (FirebaseApp.getApps().isEmpty()) {
            System.err.println("[NotificationService] ERROR: Firebase NO está inicializado. No se puede enviar notificación.")
            System.err.println("[NotificationService] Verifica que FIREBASE_CREDENTIALS esté configurada o firebase-admin.json exista en resources.")
            return
        }
        
        if (token.isBlank()) {
            System.err.println("[NotificationService] ERROR: Token FCM vacío. No se puede enviar notificación.")
            return
        }
        
        try {
            println("[NotificationService] Enviando notificación a token: ${token.take(15)}...")
            println("[NotificationService] Título: $title")
            
            val notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build()

            val androidConfig = com.google.firebase.messaging.AndroidConfig.builder()
                .setPriority(com.google.firebase.messaging.AndroidConfig.Priority.HIGH)
                .setNotification(
                    com.google.firebase.messaging.AndroidNotification.builder()
                        .setChannelId("agroflow_tasks_channel_v2")
                        .setPriority(com.google.firebase.messaging.AndroidNotification.Priority.MAX)
                        .setSound("default")
                        .setDefaultVibrateTimings(true)
                        .build()
                )
                .build()

            val message = Message.builder()
                .setToken(token)
                .setNotification(notification)
                .putData("title", title)
                .putData("body", body)
                .setAndroidConfig(androidConfig)
                .build()

            val response = FirebaseMessaging.getInstance().send(message)
            println("[NotificationService] Notificación enviada exitosamente. Response ID: $response")
        } catch (e: FirebaseMessagingException) {
            System.err.println("[NotificationService] ERROR Firebase: ${e.messagingErrorCode} - ${e.message}")
            e.printStackTrace()
        } catch (e: Exception) {
            System.err.println("[NotificationService] ERROR inesperado: ${e.javaClass.simpleName} - ${e.message}")
            e.printStackTrace()
        }
    }
}


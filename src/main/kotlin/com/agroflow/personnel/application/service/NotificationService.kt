package com.agroflow.personnel.application.service

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import org.springframework.stereotype.Service

@Service
class NotificationService {

    fun sendPushNotification(token: String, title: String, body: String) {
        try {
            val notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build()

            val message = Message.builder()
                .setToken(token)
                .setNotification(notification)
                .build()

            val response = FirebaseMessaging.getInstance().send(message)
            println("Notificacion enviada correctamente: $response")
        } catch (e: Exception) {
            println("Error enviando notificacion: $e")
            throw e
        }
    }
}

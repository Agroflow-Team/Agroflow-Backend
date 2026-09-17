package com.agroflow.personnel.application.service

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import com.google.firebase.messaging.AndroidConfig
import com.google.firebase.messaging.AndroidNotification
import org.springframework.stereotype.Service

@Service
class NotificationService {

    fun sendPushNotification(token: String, title: String, body: String) {
        try {
            val message = Message.builder()
                .setToken(token)
                .setNotification(
                    Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build()
                )
                .putData("title", title)
                .putData("body", body)
                .putData("click_action", "OPEN_ACTIVITY")
                .setAndroidConfig(
                    AndroidConfig.builder()
                        .setPriority(AndroidConfig.Priority.HIGH)
                        .setNotification(
                            AndroidNotification.builder()
                                .setTitle(title)
                                .setBody(body)
                                .setChannelId("agroflow_notifications_channel")
                                .setPriority(AndroidNotification.Priority.HIGH)
                                .build()
                        )
                        .build()
                )
                .build()

            val response = FirebaseMessaging.getInstance().send(message)
            println("NotificationService: ✅ Notificación enviada correctamente con ID: $response a token (${token.take(12)}...)")
        } catch (e: Exception) {
            println("NotificationService: ❌ Error enviando notificación FCM: ${e.message}")
            e.printStackTrace()
        }
    }
}

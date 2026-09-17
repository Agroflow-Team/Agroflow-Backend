package com.agroflow.auth.application.service

import com.agroflow.personnel.infrastructure.adapter.out.persistence.SpringDataTrabajadorRepository
import com.agroflow.personnel.infrastructure.adapter.out.persistence.SpringDataUsuarioRepository
import com.agroflow.personnel.infrastructure.adapter.out.persistence.UsuarioEntity
import com.agroflow.security.jwt.JwtUtils
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val usuarioRepository: SpringDataUsuarioRepository,
    private val trabajadorRepository: SpringDataTrabajadorRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtils: JwtUtils
) {
    fun login(correo: String, clave: String, fcmToken: String? = null): Map<String, String> {
        val userEntity = usuarioRepository.findByCorreo(correo)
            .orElseThrow { RuntimeException("User not found") }

        val isPasswordMatch = passwordEncoder.matches(clave, userEntity.claveHash) || clave == userEntity.claveHash
        
        if (!isPasswordMatch) {
            throw RuntimeException("Invalid credentials")
        }

        if (fcmToken != null && userEntity.fcmToken != fcmToken) {
            userEntity.fcmToken = fcmToken
            usuarioRepository.save(userEntity)
        }

        val token = jwtUtils.generateToken(userEntity.id.toString(), userEntity.rolId.toString())

        val response = mutableMapOf(
            "token" to token,
            "usuarioId" to userEntity.id.toString(),
            "correo" to userEntity.correo,
            "rolId" to userEntity.rolId.toString()
        )

        // Si es un trabajador, adjuntamos su fincaId y su ID de trabajador
        val userIdVal = userEntity.id
        if (userIdVal != null) {
            val trabajador = trabajadorRepository.findByUsuarioId(userIdVal)
            if (trabajador.isPresent) {
                val t = trabajador.get()
                response["fincaId"] = t.fincaId.toString()
                if (t.id != null) {
                    response["trabajadorId"] = t.id.toString()
                }
            }
        }

        return response
    }

    fun updateFcmToken(usuarioId: String, fcmToken: String) {
        val userUuid = try {
            java.util.UUID.fromString(usuarioId)
        } catch (e: Exception) {
            throw IllegalArgumentException("Invalid UUID: $usuarioId")
        }
        val userEntity = usuarioRepository.findById(userUuid)
            .orElseThrow { RuntimeException("User not found with id: $usuarioId") }

        userEntity.fcmToken = fcmToken
        usuarioRepository.save(userEntity)
        println("AuthService: FCM Token actualizado exitosamente para usuario ${userEntity.correo}")
    }
}

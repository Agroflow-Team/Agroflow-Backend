package com.agroflow.auth.application.service

import com.agroflow.personnel.infrastructure.adapter.out.persistence.SpringDataUsuarioRepository
import com.agroflow.personnel.infrastructure.adapter.out.persistence.UsuarioEntity
import com.agroflow.security.jwt.JwtUtils
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val usuarioRepository: SpringDataUsuarioRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtils: JwtUtils
) {
    fun login(correo: String, clave: String): Map<String, String> {
        val userEntity = usuarioRepository.findByCorreo(correo)
            .orElseThrow { RuntimeException("User not found") }

        val isPasswordMatch = passwordEncoder.matches(clave, userEntity.claveHash) || clave == userEntity.claveHash
        
        if (!isPasswordMatch) {
            throw RuntimeException("Invalid credentials")
        }

        val token = jwtUtils.generateToken(userEntity.id.toString(), userEntity.rolId.toString())

        return mapOf(
            "token" to token,
            "usuarioId" to userEntity.id.toString(),
            "correo" to userEntity.correo,
            "rolId" to userEntity.rolId.toString()
        )
    }
}

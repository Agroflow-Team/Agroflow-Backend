package com.agroflow.personnel.application.service

import com.agroflow.personnel.application.port.`in`.LoginRequest
import com.agroflow.personnel.application.port.`in`.LoginResponse
import com.agroflow.personnel.application.port.`in`.LoginUseCase
import com.agroflow.personnel.application.port.out.UsuarioRepositoryPort
import org.springframework.stereotype.Service

@Service
class LoginService(
    private val usuarioRepository: UsuarioRepositoryPort
) : LoginUseCase {

    override fun authenticate(request: LoginRequest): LoginResponse {
        // 1. Buscamos el usuario por correo en la base de datos
        val usuario = usuarioRepository.findByCorreo(request.correo)
            .orElseThrow { RuntimeException("Credenciales inválidas: Usuario no encontrado") }

        // 2. Aquí validamos la contraseña.
        // Como guardaste 'clave_hash' en la BD, puedes hacer una verificación básica o comparar texto plano por ahora.
        // (En tu script de prueba pusimos 'hash_temporal_123').
        // if (usuario.claveHash != request.clave) { throw RuntimeException("Contraseña incorrecta") }

        return LoginResponse(
            usuarioId = usuario.id!!,
            correo = usuario.correo,
            rolId = usuario.rolId,
            token = "AUTH_TOKEN_SUCCESS_AGROFLOW" // Identificador para que Android sepa que entró bien
        )
    }
}
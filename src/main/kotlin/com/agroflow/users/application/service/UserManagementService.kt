package com.agroflow.users.application.service

import com.agroflow.core.domain.Roles
import com.agroflow.personnel.application.port.out.TrabajadorRepositoryPort
import com.agroflow.personnel.application.port.out.UsuarioRepositoryPort
import com.agroflow.personnel.domain.model.Trabajador
import com.agroflow.personnel.infrastructure.adapter.out.persistence.UsuarioEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Service
class UserManagementService(
    private val usuarioRepository: UsuarioRepositoryPort,
    private val trabajadorRepository: TrabajadorRepositoryPort,
    private val passwordEncoder: PasswordEncoder
) {
    fun createCliente(correo: String, clave: String, name: String): UsuarioEntity {
        val encodedPassword = passwordEncoder.encode(clave) ?: ""
        val usuario = UsuarioEntity(
            id = UUID.randomUUID(),
            rolId = Roles.CLIENTE,
            correo = correo,
            claveHash = encodedPassword,
            estado = "ACTIVO",
            fechaCreacion = LocalDateTime.now()
        )
        return usuarioRepository.save(usuario)
    }

    fun createAdminOrAgricultor(correo: String, clave: String, requestedRole: String): UsuarioEntity {
        val rolId = when (requestedRole.uppercase()) {
            "ADMIN" -> Roles.ADMIN
            "AGRICULTOR" -> Roles.AGRICULTOR
            else -> throw IllegalArgumentException("Role not allowed: $requestedRole")
        }

        val encodedPassword = passwordEncoder.encode(clave) ?: ""
        val usuario = UsuarioEntity(
            id = UUID.randomUUID(),
            rolId = rolId,
            correo = correo,
            claveHash = encodedPassword,
            estado = "ACTIVO",
            fechaCreacion = LocalDateTime.now()
        )
        return usuarioRepository.save(usuario)
    }

    fun createAgricultorOrTrabajador(
        correo: String,
        clave: String,
        requestedRole: String,
        fincaId: UUID?,
        nombreCompleto: String?,
        documento: String?,
        tarifaHora: BigDecimal?
    ): UsuarioEntity {
        val rolId = when (requestedRole.uppercase()) {
            "AGRICULTOR" -> Roles.AGRICULTOR
            "TRABAJADOR" -> Roles.TRABAJADOR
            else -> throw IllegalArgumentException("Role not allowed: $requestedRole")
        }

        val encodedPassword = passwordEncoder.encode(clave) ?: ""
        val usuarioId = UUID.randomUUID()
        val usuario = UsuarioEntity(
            id = usuarioId,
            rolId = rolId,
            correo = correo,
            claveHash = encodedPassword,
            estado = "ACTIVO",
            fechaCreacion = LocalDateTime.now()
        )
        val savedUser = usuarioRepository.save(usuario)

        if (rolId == Roles.TRABAJADOR) {
            val nuevoTrabajador = Trabajador(
                id = UUID.randomUUID(),
                usuarioId = usuarioId,
                fincaId = fincaId ?: UUID.randomUUID(),
                nombreCompleto = nombreCompleto ?: "Trabajador",
                documento = documento ?: "00000000",
                tarifaHora = tarifaHora ?: BigDecimal.ZERO,
                fechaRegistro = LocalDateTime.now()
            )
            trabajadorRepository.save(nuevoTrabajador)
        }

        return savedUser
    }
}

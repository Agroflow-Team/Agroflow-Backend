package com.agroflow.personnel.infrastructure.adapter.out.persistence

import com.agroflow.personnel.application.port.out.UsuarioRepositoryPort
import com.agroflow.personnel.domain.model.Usuario
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
class UsuarioRepositoryAdapter(
    private val repository: SpringDataUsuarioRepository
) : UsuarioRepositoryPort {
    override fun findByCorreo(correo: String): Optional<Usuario> {
        return repository.findByCorreo(correo).map { it.toDomain() }
    }

    override fun findIdByCorreo(correo: String): Optional<UUID> {
        return repository.findByCorreo(correo).map { it.id }
    }
}
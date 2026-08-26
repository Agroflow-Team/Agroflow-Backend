package com.agroflow.personnel.infrastructure.adapter.out.persistence

import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import java.util.UUID

interface SpringDataUsuarioRepository : JpaRepository<UsuarioEntity, UUID> {
    fun findByCorreo(correo: String): Optional<UsuarioEntity>
}
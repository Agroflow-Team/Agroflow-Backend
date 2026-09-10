package com.agroflow.personnel.application.port.out

import com.agroflow.personnel.domain.model.Usuario
import com.agroflow.personnel.infrastructure.adapter.out.persistence.UsuarioEntity
import java.util.Optional
import java.util.UUID

interface UsuarioRepositoryPort {
    fun findByCorreo(correo: String): Optional<Usuario>
    fun findIdByCorreo(correo: String): Optional<UUID>
    fun findById(id: UUID): Optional<UsuarioEntity>
    fun save(entity: UsuarioEntity): UsuarioEntity
}

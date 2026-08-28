package com.agroflow.personnel.application.port.out

import com.agroflow.personnel.domain.model.Usuario
import com.agroflow.personnel.infrastructure.adapter.out.persistence.SpringDataTaskRepository
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import java.util.UUID

interface UsuarioRepositoryPort  {
    fun findByCorreo(correo: String): Optional<Usuario>
    fun findIdByCorreo(correo: String): Optional<UUID>
}

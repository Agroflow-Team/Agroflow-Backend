package com.agroflow.personnel.infrastructure.adapter.out.persistence

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface SpringDataTrabajadorRepository : JpaRepository<TrabajadorEntity, UUID> {
    fun findByFincaId(fincaId: UUID): List<TrabajadorEntity>
    fun findByUsuarioId(usuarioId: UUID): java.util.Optional<TrabajadorEntity>
}
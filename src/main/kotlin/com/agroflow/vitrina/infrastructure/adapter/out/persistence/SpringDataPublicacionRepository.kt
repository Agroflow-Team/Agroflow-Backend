package com.agroflow.vitrina.infrastructure.adapter.out.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface SpringDataPublicacionRepository : JpaRepository<PublicacionEntity, UUID> {
    fun findByEstadoPublicacion(estadoPublicacion: String): List<PublicacionEntity>
    fun findByFincaId(fincaId: UUID): List<PublicacionEntity>
}

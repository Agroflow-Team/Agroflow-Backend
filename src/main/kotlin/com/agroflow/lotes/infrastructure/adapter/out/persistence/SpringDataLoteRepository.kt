package com.agroflow.lotes.infrastructure.adapter.out.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface SpringDataLoteRepository : JpaRepository<LoteEntity, UUID> {
    fun findByFincaId(fincaId: UUID): List<LoteEntity>
}

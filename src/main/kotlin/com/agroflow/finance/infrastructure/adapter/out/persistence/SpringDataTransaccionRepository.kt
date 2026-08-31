package com.agroflow.finance.infrastructure.adapter.out.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface SpringDataTransaccionRepository : JpaRepository<TransaccionEntity, UUID> {
    fun findByFincaId(fincaId: UUID): List<TransaccionEntity>
}

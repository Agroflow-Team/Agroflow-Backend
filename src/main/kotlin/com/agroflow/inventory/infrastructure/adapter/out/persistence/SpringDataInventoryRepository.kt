package com.agroflow.inventory.infrastructure.adapter.out.persistence

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface SpringDataInventoryRepository : JpaRepository<InventoryEntity, UUID> {
    // Busca los items de una finca que no esten eliminados
    fun findByFincaIdAndEliminadoFalse(fincaId: UUID): List<InventoryEntity>
}
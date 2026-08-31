package com.agroflow.inventory.application.port.out

import com.agroflow.inventory.domain.model.InventoryItem
import java.util.UUID

interface InventoryRepositoryPort {
    fun save(item: InventoryItem): InventoryItem
    fun findById(id: UUID): InventoryItem?
    fun findByFincaId(fincaId: UUID): List<InventoryItem>
    fun deleteById(id: UUID)
}
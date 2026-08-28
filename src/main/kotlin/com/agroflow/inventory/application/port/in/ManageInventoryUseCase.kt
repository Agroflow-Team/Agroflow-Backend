package com.agroflow.inventory.application.port.`in`

import com.agroflow.inventory.domain.model.InventoryItem
import java.math.BigDecimal
import java.util.UUID

interface ManageInventoryUseCase {
    fun getByFinca(fincaId: UUID): List<InventoryItem>
    fun updateStock(itemId: UUID, cantidadUsada: BigDecimal): InventoryItem
    fun updateItem(itemId: UUID, nombreItem: String, cantidad: BigDecimal, unidadMedida: String): InventoryItem
}
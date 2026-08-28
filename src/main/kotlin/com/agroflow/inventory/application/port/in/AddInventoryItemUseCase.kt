package com.agroflow.inventory.application.port.`in`

import com.agroflow.inventory.domain.model.InventoryItem

interface AddInventoryItemUseCase {
    fun add(item: InventoryItem): InventoryItem
}
package com.agroflow.inventory.application.service

import com.agroflow.inventory.application.port.`in`.AddInventoryItemUseCase
import com.agroflow.inventory.application.port.`in`.ManageInventoryUseCase
import com.agroflow.inventory.application.port.out.InventoryRepositoryPort
import com.agroflow.inventory.domain.model.InventoryItem
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Service
class InventoryService(
    private val repository: InventoryRepositoryPort
) : AddInventoryItemUseCase, ManageInventoryUseCase {

    override fun add(item: InventoryItem): InventoryItem {
        return repository.save(item)
    }

    override fun getByFinca(fincaId: UUID): List<InventoryItem> {
        return repository.findByFincaId(fincaId)
    }

    override fun updateStock(itemId: UUID, cantidadUsada: BigDecimal): InventoryItem {
        // 1. Buscamos el item original
        val item = repository.findById(itemId)
            ?: throw RuntimeException("Item de inventario no encontrado")

        // 2. Aplicamos la logica de negocio
        val nuevaCantidad = item.cantidad.subtract(cantidadUsada)
        if (nuevaCantidad < BigDecimal.ZERO) {
            throw RuntimeException("No hay suficiente stock de ${item.nombreItem}")
        }

        // 3. Creamos una copia actualizada
        val updatedItem = item.copy(
            cantidad = nuevaCantidad,
            fechaActualizacion = LocalDateTime.now()
        )

        // 4. Guardamos los cambios
        return repository.save(updatedItem)
    }

    override fun updateItem(itemId: UUID, nombreItem: String, cantidad: BigDecimal, unidadMedida: String): InventoryItem {
        val item = repository.findById(itemId)
            ?: throw RuntimeException("Item de inventario no encontrado")

        val updatedItem = item.copy(
            nombreItem = nombreItem,
            cantidad = cantidad,
            unidadMedida = unidadMedida,
            fechaActualizacion = LocalDateTime.now()
        )

        return repository.save(updatedItem)
    }
}
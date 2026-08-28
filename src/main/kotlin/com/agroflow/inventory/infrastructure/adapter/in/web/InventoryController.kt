package com.agroflow.inventory.infrastructure.adapter.`in`.web

import com.agroflow.inventory.application.port.`in`.AddInventoryItemUseCase
import com.agroflow.inventory.application.port.`in`.ManageInventoryUseCase
import com.agroflow.inventory.domain.model.EstadoSincronizacionEnum
import com.agroflow.inventory.domain.model.InventoryItem
import com.agroflow.inventory.domain.model.TipoItemEnum
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.util.UUID

@RestController
@RequestMapping("/api/inventory")
class InventoryController(
    private val addInventoryItemUseCase: AddInventoryItemUseCase,
    private val manageInventoryUseCase: ManageInventoryUseCase
) {

    // Crear un nuevo item en el inventario (POST /api/inventory)
    @PostMapping
    fun addItem(@RequestBody request: CreateInventoryItemRequest): ResponseEntity<InventoryItem> {
        val newItem = InventoryItem(
            fincaId = request.fincaId,
            registradoPorTrabajadorId = request.registradoPorTrabajadorId,
            nombreItem = request.nombreItem,
            tipo = request.tipo,
            cantidad = request.cantidad,
            unidadMedida = request.unidadMedida,
            costoUnitario = request.costoUnitario,
            estadoSincronizacion = EstadoSincronizacionEnum.SINCRONIZADO
        )
        val created = addInventoryItemUseCase.add(newItem)
        return ResponseEntity.ok(created)
    }

    // Listar inventario por finca (GET /api/inventory/finca/{fincaId})
    @GetMapping("/finca/{fincaId}")
    fun getInventoryByFinca(@PathVariable fincaId: UUID): ResponseEntity<List<InventoryItem>> {
        val items = manageInventoryUseCase.getByFinca(fincaId)
        return ResponseEntity.ok(items)
    }

    // Actualizar / Descontar stock (PATCH /api/inventory/{itemId}/stock)
    @PatchMapping("/{itemId}/stock")
    fun updateStock(
        @PathVariable itemId: UUID,
        @RequestBody request: UpdateStockRequest
    ): ResponseEntity<InventoryItem> {
        val updated = manageInventoryUseCase.updateStock(itemId, request.cantidadUsada)
        return ResponseEntity.ok(updated)
    }

    // Editar un ítem completo (PUT /api/inventory/{itemId})
    @PutMapping("/{itemId}")
    fun editItem(
        @PathVariable itemId: UUID,
        @RequestBody request: UpdateInventoryItemRequest
    ): ResponseEntity<InventoryItem> {
        val updated = manageInventoryUseCase.updateItem(itemId, request.nombreItem, request.cantidad, request.unidadMedida)
        return ResponseEntity.ok(updated)
    }
}

data class CreateInventoryItemRequest(
    val fincaId: UUID,
    val registradoPorTrabajadorId: UUID,
    val nombreItem: String,
    val tipo: TipoItemEnum,
    val cantidad: BigDecimal,
    val unidadMedida: String,
    val costoUnitario: BigDecimal? = null
)

data class UpdateStockRequest(
    val cantidadUsada: BigDecimal
)

data class UpdateInventoryItemRequest(
    val nombreItem: String,
    val cantidad: BigDecimal,
    val unidadMedida: String
)

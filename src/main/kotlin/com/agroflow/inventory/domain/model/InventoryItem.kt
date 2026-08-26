package com.agroflow.inventory.domain.model

import java.util.UUID
import java.time.LocalDateTime
import java.math.BigDecimal

class InventoryItem(
    val id: UUID? = null,
    val fincaId: UUID,
    val registradoPorTrabajadorId: UUID,
    var nombreItem: String,
    var tipo: String, // Podria ser un Enum mas adelante si lo deseas
    var cantidad: BigDecimal,
    var unidadMedida: String,
    var costoUnitario: BigDecimal,
    var fechaActualizacion: LocalDateTime = LocalDateTime.now(),
    var eliminado: Boolean = false,
    var estadoSincronizacion: String = "PENDIENTE"
) {
    // Ejemplo de regla de negocio en el dominio
    fun actualizarCantidad(nuevaCantidad: BigDecimal) {
        if (nuevaCantidad < BigDecimal.ZERO) {
            throw IllegalArgumentException("La cantidad no puede ser negativa")
        }
        this.cantidad = nuevaCantidad
        this.fechaActualizacion = LocalDateTime.now()
        this.estadoSincronizacion = "MODIFICADO"
    }
}
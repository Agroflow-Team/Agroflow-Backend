package com.agroflow.inventory.domain.model

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

enum class TipoItemEnum {
    // Reemplazar con los valores exactos del tipo_item_enum en Postgres
    INSUMO, HERRAMIENTA, MAQUINARIA, SEMILLA
}

enum class EstadoSincronizacionEnum {
    PENDIENTE, SINCRONIZADO, ERROR
}

data class InventoryItem(
    val id: UUID? = null,
    val fincaId: UUID,
    val registradoPorTrabajadorId: UUID,
    val nombreItem: String,
    val tipo: TipoItemEnum,
    val cantidad: BigDecimal,
    val unidadMedida: String,
    val fechaActualizacion: LocalDateTime = LocalDateTime.now(),
    val eliminado: Boolean = false,
    val costoUnitario: BigDecimal? = null,
    val estadoSincronizacion: EstadoSincronizacionEnum = EstadoSincronizacionEnum.SINCRONIZADO
)
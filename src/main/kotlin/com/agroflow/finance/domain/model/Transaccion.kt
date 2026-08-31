package com.agroflow.finance.domain.model

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

enum class TransaccionTipo {
    INGRESO, EGRESO
}

enum class TransaccionCategoria {
    VENTA_FISICA, INSUMO, SALARIO, OTRO
}

data class Transaccion(
    val id: UUID? = null,
    val fincaId: UUID,
    val tipoMovimiento: TransaccionTipo,
    val categoria: TransaccionCategoria,
    val montoTotal: BigDecimal,
    val fechaTransaccion: LocalDateTime,
    val estadoSincronizacion: String = "SINCRONIZADO"
)

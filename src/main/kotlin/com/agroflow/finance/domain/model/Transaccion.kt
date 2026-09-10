package com.agroflow.finance.domain.model

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

enum class TransaccionTipo {
    INGRESO, EGRESO
}


data class Transaccion(
    val id: UUID? = null,
    val fincaId: UUID,
    val tipoMovimiento: TransaccionTipo,
    val categoria: String,
    val montoTotal: BigDecimal,
    val fechaTransaccion: LocalDateTime,
    val estadoSincronizacion: String = "SINCRONIZADO"
)

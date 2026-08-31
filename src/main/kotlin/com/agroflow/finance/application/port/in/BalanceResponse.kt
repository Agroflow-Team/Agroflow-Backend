package com.agroflow.finance.application.port.`in`

import com.agroflow.finance.domain.model.Transaccion
import java.math.BigDecimal

data class BalanceResponse(
    val totalIngresos: BigDecimal,
    val totalEgresos: BigDecimal,
    val utilidadNeta: BigDecimal,
    val transacciones: List<Transaccion>
)

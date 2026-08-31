package com.agroflow.finance.application.port.`in`

import com.agroflow.finance.domain.model.Transaccion
import java.util.UUID

interface ManageFinanzasUseCase {
    fun registrarTransaccion(transaccion: Transaccion): Transaccion
    fun getBalanceFinca(fincaId: UUID): BalanceResponse
}

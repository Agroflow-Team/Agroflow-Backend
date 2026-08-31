package com.agroflow.finance.application.service

import com.agroflow.finance.application.port.`in`.BalanceResponse
import com.agroflow.finance.application.port.`in`.ManageFinanzasUseCase
import com.agroflow.finance.application.port.out.TransaccionRepositoryPort
import com.agroflow.finance.domain.model.Transaccion
import com.agroflow.finance.domain.model.TransaccionTipo
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.UUID

@Service
class FinanzasService(
    private val transaccionRepository: TransaccionRepositoryPort
) : ManageFinanzasUseCase {

    override fun registrarTransaccion(transaccion: Transaccion): Transaccion {
        return transaccionRepository.save(transaccion)
    }

    override fun getBalanceFinca(fincaId: UUID): BalanceResponse {
        val transacciones = transaccionRepository.findByFincaId(fincaId)
        
        var ingresos = BigDecimal.ZERO
        var egresos = BigDecimal.ZERO
        
        for (t in transacciones) {
            when (t.tipoMovimiento) {
                TransaccionTipo.INGRESO -> ingresos = ingresos.add(t.montoTotal)
                TransaccionTipo.EGRESO -> egresos = egresos.add(t.montoTotal)
            }
        }
        
        val utilidadNeta = ingresos.subtract(egresos)
        
        return BalanceResponse(
            totalIngresos = ingresos,
            totalEgresos = egresos,
            utilidadNeta = utilidadNeta,
            transacciones = transacciones
        )
    }
}

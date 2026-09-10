package com.agroflow.finance.infrastructure.adapter.`in`.web

import com.agroflow.finance.application.port.`in`.BalanceResponse
import com.agroflow.finance.application.port.`in`.ManageFinanzasUseCase
import com.agroflow.finance.domain.model.Transaccion

import com.agroflow.finance.domain.model.TransaccionTipo
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@RestController
@RequestMapping("/api/finanzas")
class FinanzasController(
    private val manageFinanzasUseCase: ManageFinanzasUseCase
) {

    @GetMapping("/finca/{fincaId}")
    fun getBalanceFinca(@PathVariable fincaId: UUID): ResponseEntity<BalanceResponse> {
        val balance = manageFinanzasUseCase.getBalanceFinca(fincaId)
        return ResponseEntity.ok(balance)
    }

    @PostMapping("")
    fun registrarTransaccion(@RequestBody request: TransaccionRequest): ResponseEntity<Transaccion> {
        val transaccion = Transaccion(
            fincaId = request.fincaId,
            tipoMovimiento = request.tipoMovimiento,
            categoria = request.categoria,
            montoTotal = request.montoTotal,
            fechaTransaccion = request.fechaTransaccion ?: LocalDateTime.now(),
            estadoSincronizacion = "SINCRONIZADO"
        )
        val created = manageFinanzasUseCase.registrarTransaccion(transaccion)
        return ResponseEntity.ok(created)
    }
}

data class TransaccionRequest(
    val fincaId: UUID,
    val tipoMovimiento: TransaccionTipo,
    val categoria: String,
    val montoTotal: BigDecimal,
    val fechaTransaccion: LocalDateTime? = null
)

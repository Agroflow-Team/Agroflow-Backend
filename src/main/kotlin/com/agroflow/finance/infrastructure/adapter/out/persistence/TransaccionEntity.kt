package com.agroflow.finance.infrastructure.adapter.out.persistence

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "transacciones")
data class TransaccionEntity(
    @Id
    val id: UUID? = null,

    @Column(name = "finca_id", nullable = false)
    val fincaId: UUID,

    @Column(name = "tipo_movimiento", nullable = false)
    val tipoMovimiento: String,

    @Column(name = "categoria", nullable = false)
    val categoria: String,

    @Column(name = "monto_total", nullable = false)
    val montoTotal: BigDecimal,

    @Column(name = "fecha_transaccion", nullable = false)
    val fechaTransaccion: LocalDateTime,

    @Column(name = "estado_sincronizacion", nullable = false)
    val estadoSincronizacion: String
)

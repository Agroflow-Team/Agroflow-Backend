package com.agroflow.finance.application.port.out

import com.agroflow.finance.domain.model.Transaccion
import java.util.UUID

interface TransaccionRepositoryPort {
    fun save(transaccion: Transaccion): Transaccion
    fun findByFincaId(fincaId: UUID): List<Transaccion>
}

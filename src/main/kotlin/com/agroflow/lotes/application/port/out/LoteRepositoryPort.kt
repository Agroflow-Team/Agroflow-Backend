package com.agroflow.lotes.application.port.out

import com.agroflow.lotes.domain.model.Lote
import java.util.UUID

interface LoteRepositoryPort {
    fun save(lote: Lote): Lote
    fun findByFincaId(fincaId: UUID): List<Lote>
}

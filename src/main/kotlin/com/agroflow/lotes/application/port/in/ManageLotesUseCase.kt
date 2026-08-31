package com.agroflow.lotes.application.port.`in`

import com.agroflow.lotes.domain.model.Lote
import java.util.UUID

interface ManageLotesUseCase {
    fun getLotesByFinca(fincaId: UUID): List<Lote>
    fun registrarLote(lote: Lote): Lote
}

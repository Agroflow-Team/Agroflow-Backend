package com.agroflow.lotes.application.service

import com.agroflow.lotes.application.port.`in`.ManageLotesUseCase
import com.agroflow.lotes.application.port.out.LoteRepositoryPort
import com.agroflow.lotes.domain.model.Lote
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class LoteService(
    private val loteRepository: LoteRepositoryPort
) : ManageLotesUseCase {

    override fun getLotesByFinca(fincaId: UUID): List<Lote> {
        return loteRepository.findByFincaId(fincaId)
    }

    override fun registrarLote(lote: Lote): Lote {
        return loteRepository.save(lote)
    }
}

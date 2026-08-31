package com.agroflow.lotes.infrastructure.adapter.out.persistence

import com.agroflow.lotes.application.port.out.LoteRepositoryPort
import com.agroflow.lotes.domain.model.Lote
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class LoteRepositoryAdapter(
    private val repository: SpringDataLoteRepository
) : LoteRepositoryPort {

    override fun save(lote: Lote): Lote {
        val entity = LoteEntity(
            id = lote.id ?: UUID.randomUUID(),
            fincaId = lote.fincaId,
            nombre = lote.nombre,
            latitud = lote.latitud,
            longitud = lote.longitud,
            fechaRegistro = lote.fechaRegistro,
            estadoSincronizacion = lote.estadoSincronizacion
        )
        val saved = repository.save(entity)
        return toDomain(saved)
    }

    override fun findByFincaId(fincaId: UUID): List<Lote> {
        return repository.findByFincaId(fincaId).map { toDomain(it) }
    }

    private fun toDomain(entity: LoteEntity): Lote {
        return Lote(
            id = entity.id,
            fincaId = entity.fincaId,
            nombre = entity.nombre,
            latitud = entity.latitud,
            longitud = entity.longitud,
            fechaRegistro = entity.fechaRegistro,
            estadoSincronizacion = entity.estadoSincronizacion
        )
    }
}

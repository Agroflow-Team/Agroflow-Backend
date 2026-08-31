package com.agroflow.finance.infrastructure.adapter.out.persistence

import com.agroflow.finance.application.port.out.TransaccionRepositoryPort
import com.agroflow.finance.domain.model.Transaccion
import com.agroflow.finance.domain.model.TransaccionCategoria
import com.agroflow.finance.domain.model.TransaccionTipo
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class TransaccionRepositoryAdapter(
    private val repository: SpringDataTransaccionRepository
) : TransaccionRepositoryPort {

    override fun save(transaccion: Transaccion): Transaccion {
        val entity = TransaccionEntity(
            id = transaccion.id ?: UUID.randomUUID(),
            fincaId = transaccion.fincaId,
            tipoMovimiento = transaccion.tipoMovimiento.name,
            categoria = transaccion.categoria.name,
            montoTotal = transaccion.montoTotal,
            fechaTransaccion = transaccion.fechaTransaccion,
            estadoSincronizacion = transaccion.estadoSincronizacion
        )
        val saved = repository.save(entity)
        return toDomain(saved)
    }

    override fun findByFincaId(fincaId: UUID): List<Transaccion> {
        return repository.findByFincaId(fincaId).map { toDomain(it) }
    }

    private fun toDomain(entity: TransaccionEntity): Transaccion {
        return Transaccion(
            id = entity.id,
            fincaId = entity.fincaId,
            tipoMovimiento = TransaccionTipo.valueOf(entity.tipoMovimiento),
            categoria = TransaccionCategoria.valueOf(entity.categoria),
            montoTotal = entity.montoTotal,
            fechaTransaccion = entity.fechaTransaccion,
            estadoSincronizacion = entity.estadoSincronizacion
        )
    }
}

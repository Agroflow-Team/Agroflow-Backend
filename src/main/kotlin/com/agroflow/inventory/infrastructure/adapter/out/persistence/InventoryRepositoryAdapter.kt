package com.agroflow.inventory.infrastructure.adapter.out.persistence

import com.agroflow.inventory.application.port.out.InventoryRepositoryPort
import com.agroflow.inventory.domain.model.InventoryItem
import com.agroflow.inventory.domain.model.TipoItemEnum
import com.agroflow.inventory.domain.model.EstadoSincronizacionEnum
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class InventoryRepositoryAdapter(
    private val repository: SpringDataInventoryRepository
) : InventoryRepositoryPort {

    override fun save(item: InventoryItem): InventoryItem {
        val entity = toEntity(item)
        val savedEntity = repository.save(entity)
        return toDomain(savedEntity)
    }

    override fun findById(id: UUID): InventoryItem? {
        return repository.findById(id).map { toDomain(it) }.orElse(null)
    }

    override fun findByFincaId(fincaId: UUID): List<InventoryItem> {
        return repository.findByFincaIdAndEliminadoFalse(fincaId).map { toDomain(it) }
    }

    override fun deleteById(id: UUID) {
        repository.findById(id).ifPresent { entity ->
            // Soft delete or delete
            val updated = InventoryEntity(
                id = entity.id,
                fincaId = entity.fincaId,
                registradoPorTrabajadorId = entity.registradoPorTrabajadorId,
                nombreItem = entity.nombreItem,
                tipo = entity.tipo,
                cantidad = entity.cantidad,
                unidadMedida = entity.unidadMedida,
                fechaActualizacion = java.time.LocalDateTime.now(),
                eliminado = true,
                costoUnitario = entity.costoUnitario,
                estadoSincronizacion = entity.estadoSincronizacion
            )
            repository.save(updated)
        }
    }

    // Transformadores (Mappers)
    private fun toEntity(domain: InventoryItem): InventoryEntity {
        return InventoryEntity(
            id = domain.id ?: UUID.randomUUID(),
            fincaId = domain.fincaId,
            registradoPorTrabajadorId = domain.registradoPorTrabajadorId,
            nombreItem = domain.nombreItem,
            tipo = domain.tipo.name,
            cantidad = domain.cantidad,
            unidadMedida = domain.unidadMedida,
            fechaActualizacion = domain.fechaActualizacion,
            eliminado = domain.eliminado,
            costoUnitario = domain.costoUnitario,
            estadoSincronizacion = domain.estadoSincronizacion.name
        )
    }

    private fun toDomain(entity: InventoryEntity): InventoryItem {
        val tipoSafe = try {
            TipoItemEnum.valueOf(entity.tipo.uppercase())
        } catch (e: Exception) {
            TipoItemEnum.INSUMO // Fallback en caso de valor desconocido
        }
        
        val estadoSafe = try {
            EstadoSincronizacionEnum.valueOf(entity.estadoSincronizacion.uppercase())
        } catch (e: Exception) {
            EstadoSincronizacionEnum.PENDIENTE
        }

        return InventoryItem(
            id = entity.id,
            fincaId = entity.fincaId,
            registradoPorTrabajadorId = entity.registradoPorTrabajadorId,
            nombreItem = entity.nombreItem,
            tipo = tipoSafe,
            cantidad = entity.cantidad,
            unidadMedida = entity.unidadMedida,
            fechaActualizacion = entity.fechaActualizacion ?: java.time.LocalDateTime.now(),
            eliminado = entity.eliminado,
            costoUnitario = entity.costoUnitario,
            estadoSincronizacion = estadoSafe
        )
    }
}
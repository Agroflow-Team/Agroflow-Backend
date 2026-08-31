package com.agroflow.vitrina.infrastructure.adapter.out.persistence

import com.agroflow.vitrina.application.port.out.PublicacionRepositoryPort
import com.agroflow.vitrina.domain.model.EstadoPublicacion
import com.agroflow.vitrina.domain.model.Publicacion
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class PublicacionRepositoryAdapter(
    private val repository: SpringDataPublicacionRepository
) : PublicacionRepositoryPort {

    override fun save(publicacion: Publicacion): Publicacion {
        val entity = PublicacionEntity(
            id = publicacion.id ?: UUID.randomUUID(),
            fincaId = publicacion.fincaId,
            tituloProducto = publicacion.tituloProducto,
            descripcion = publicacion.descripcion,
            precio = publicacion.precio,
            cantidadDisponible = publicacion.cantidadDisponible,
            imagenUrl = publicacion.imagenUrl,
            estadoPublicacion = publicacion.estadoPublicacion.name,
            fechaCreacion = publicacion.fechaCreacion,
            estadoSincronizacion = publicacion.estadoSincronizacion
        )
        val saved = repository.save(entity)
        return toDomain(saved)
    }

    override fun findById(id: UUID): Publicacion? {
        return repository.findByIdOrNull(id)?.let { toDomain(it) }
    }

    override fun findByEstado(estado: EstadoPublicacion): List<Publicacion> {
        return repository.findByEstadoPublicacion(estado.name).map { toDomain(it) }
    }

    override fun findByFincaId(fincaId: UUID): List<Publicacion> {
        return repository.findByFincaId(fincaId).map { toDomain(it) }
    }

    private fun toDomain(entity: PublicacionEntity): Publicacion {
        return Publicacion(
            id = entity.id,
            fincaId = entity.fincaId,
            tituloProducto = entity.tituloProducto,
            descripcion = entity.descripcion,
            precio = entity.precio,
            cantidadDisponible = entity.cantidadDisponible,
            imagenUrl = entity.imagenUrl,
            estadoPublicacion = EstadoPublicacion.valueOf(entity.estadoPublicacion),
            fechaCreacion = entity.fechaCreacion,
            estadoSincronizacion = entity.estadoSincronizacion
        )
    }
}

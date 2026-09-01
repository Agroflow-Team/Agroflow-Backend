package com.agroflow.vitrina.application.service

import com.agroflow.vitrina.application.port.`in`.ManageVitrinaUseCase
import com.agroflow.vitrina.application.port.out.PublicacionRepositoryPort
import com.agroflow.vitrina.domain.model.EstadoPublicacion
import com.agroflow.vitrina.domain.model.Publicacion
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class VitrinaService(
    private val publicacionRepository: PublicacionRepositoryPort
) : ManageVitrinaUseCase {

    override fun getPublicacionesActivas(): List<Publicacion> {
        return publicacionRepository.findByEstado(EstadoPublicacion.ACTIVA)
    }

    override fun getPublicacionesByFinca(fincaId: UUID): List<Publicacion> {
        return publicacionRepository.findByFincaId(fincaId)
    }

    override fun crearPublicacion(publicacion: Publicacion): Publicacion {
        return publicacionRepository.save(publicacion)
    }

    override fun editarPublicacion(id: UUID, publicacion: Publicacion): Publicacion {
        val existing = publicacionRepository.findById(id)
            ?: throw IllegalArgumentException("Publicacion con id $id no encontrada")

        val updated = existing.copy(
            tituloProducto = publicacion.tituloProducto,
            descripcion = publicacion.descripcion,
            precio = publicacion.precio,
            cantidadDisponible = publicacion.cantidadDisponible,
            imagenUrl = publicacion.imagenUrl
        )
        return publicacionRepository.save(updated)
    }

    override fun cambiarEstado(id: UUID, estado: EstadoPublicacion): Publicacion {
        val existing = publicacionRepository.findById(id)
            ?: throw IllegalArgumentException("Publicacion no encontrada")
        val updated = existing.copy(estadoPublicacion = estado)
        return publicacionRepository.save(updated)
    }

    override fun eliminarPublicacion(id: UUID) {
        publicacionRepository.deleteById(id)
    }
}

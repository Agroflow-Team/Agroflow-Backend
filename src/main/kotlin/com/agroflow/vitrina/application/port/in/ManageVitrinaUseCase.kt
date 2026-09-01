package com.agroflow.vitrina.application.port.`in`

import com.agroflow.vitrina.domain.model.EstadoPublicacion
import com.agroflow.vitrina.domain.model.Publicacion
import java.util.UUID

interface ManageVitrinaUseCase {
    fun getPublicacionesActivas(): List<Publicacion>
    fun getPublicacionesByFinca(fincaId: UUID): List<Publicacion>
    fun crearPublicacion(publicacion: Publicacion): Publicacion
    fun editarPublicacion(id: UUID, publicacion: Publicacion): Publicacion
    fun cambiarEstado(id: UUID, estado: EstadoPublicacion): Publicacion
    fun eliminarPublicacion(id: UUID)
}

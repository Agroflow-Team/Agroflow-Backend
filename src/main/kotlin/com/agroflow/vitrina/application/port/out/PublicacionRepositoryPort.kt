package com.agroflow.vitrina.application.port.out

import com.agroflow.vitrina.domain.model.Publicacion
import com.agroflow.vitrina.domain.model.EstadoPublicacion
import java.util.UUID

interface PublicacionRepositoryPort {
    fun save(publicacion: Publicacion): Publicacion
    fun findById(id: UUID): Publicacion?
    fun findByEstado(estado: EstadoPublicacion): List<Publicacion>
    fun findByFincaId(fincaId: UUID): List<Publicacion>
}

package com.agroflow.personnel.infrastructure.adapter.out.persistence

import com.agroflow.personnel.application.port.out.TrabajadorRepositoryPort
import com.agroflow.personnel.domain.model.Trabajador
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class TrabajadorRepositoryAdapter(
    private val repository: SpringDataTrabajadorRepository
) : TrabajadorRepositoryPort {
    override val findAll: Any
        get() = TODO("Not yet implemented")

    override fun findAll(): List<Trabajador> {
        return repository.findAll().map { it.toDomain() }
    }

    override fun findByFincaId(fincaId: UUID): List<Trabajador> {
        return repository.findByFincaId(fincaId).map { it.toDomain() }
    }
}
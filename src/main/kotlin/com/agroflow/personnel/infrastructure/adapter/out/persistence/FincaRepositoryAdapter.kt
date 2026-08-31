package com.agroflow.personnel.infrastructure.adapter.out.persistence

import com.agroflow.personnel.application.port.out.FincaRepositoryPort
import com.agroflow.personnel.domain.model.Finca
import org.springframework.stereotype.Repository

@Repository
class FincaRepositoryAdapter(
    private val repository: SpringDataFincaRepository
) : FincaRepositoryPort {
    override fun findAll(): List<Finca> {
        return repository.findAll().map { it.toDomain() }
    }

    override fun save(finca: Finca): Finca {
        val entity = FincaEntity.fromDomain(finca)
        return repository.save(entity).toDomain()
    }
}
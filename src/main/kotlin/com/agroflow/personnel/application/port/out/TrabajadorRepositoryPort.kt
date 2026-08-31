package com.agroflow.personnel.application.port.out

import com.agroflow.personnel.domain.model.Trabajador
import java.util.UUID

interface TrabajadorRepositoryPort {
    fun findAll(): List<Trabajador>
    fun findByFincaId(fincaId: UUID): List<Trabajador>
    fun save(trabajador: Trabajador): Trabajador
    fun findById(id: UUID): Trabajador?
    fun deleteById(id: UUID)
}
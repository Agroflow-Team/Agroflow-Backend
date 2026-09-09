package com.agroflow.personnel.application.port.out

import com.agroflow.personnel.domain.model.Finca
import java.util.UUID
interface FincaRepositoryPort {
    fun findAll(): List<Finca>
    fun save(finca: Finca): Finca
    fun deleteById(id: UUID)
}
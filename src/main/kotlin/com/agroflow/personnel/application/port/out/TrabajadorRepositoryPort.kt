package com.agroflow.personnel.application.port.out

import com.agroflow.personnel.domain.model.Trabajador
import java.util.UUID

interface TrabajadorRepositoryPort {
    val findAll: Any
    fun findAll(): List<Trabajador>
    fun findByFincaId(fincaId: UUID): List<Trabajador>
}
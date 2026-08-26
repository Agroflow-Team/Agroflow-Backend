package com.agroflow.personnel.application.port.out

import com.agroflow.personnel.domain.model.Finca

interface FincaRepositoryPort {
    fun findAll(): List<Finca>
}
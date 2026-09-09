package com.agroflow.personnel.application.port.`in`

import com.agroflow.personnel.domain.model.Finca
import java.util.UUID

interface FindFincasUseCase {
    fun getAllFincas(): List<Finca>
    fun createFinca(nombre: String): Finca
    fun deleteFinca(id: UUID)
}
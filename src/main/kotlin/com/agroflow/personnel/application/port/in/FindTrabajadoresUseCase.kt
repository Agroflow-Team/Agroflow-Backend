package com.agroflow.personnel.application.port.`in`

import com.agroflow.personnel.domain.model.Trabajador
import java.util.UUID

interface FindTrabajadoresUseCase {
    fun getAllTrabajadores(): List<Trabajador>
    fun getTrabajadoresByFinca(fincaId: UUID): List<Trabajador>
}
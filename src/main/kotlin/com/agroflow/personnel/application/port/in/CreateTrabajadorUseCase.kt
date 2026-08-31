package com.agroflow.personnel.application.port.`in`

import com.agroflow.personnel.domain.model.Trabajador
import java.math.BigDecimal
import java.util.UUID

interface CreateTrabajadorUseCase {
    fun createTrabajador(
        fincaId: UUID,
        nombreCompleto: String,
        documento: String,
        tarifaHora: BigDecimal,
        correo: String,
        clave: String
    ): Trabajador
}

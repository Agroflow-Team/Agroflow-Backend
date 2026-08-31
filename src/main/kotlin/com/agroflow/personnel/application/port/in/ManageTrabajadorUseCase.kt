package com.agroflow.personnel.application.port.`in`

import com.agroflow.personnel.domain.model.Trabajador
import java.math.BigDecimal
import java.util.UUID

data class TrabajadorSummaryResponse(
    val trabajadorId: UUID,
    val nombreCompleto: String,
    val tarifaHora: BigDecimal,
    val totalHorasTrabajadas: BigDecimal,
    val salarioEstimado: BigDecimal,
    val tareasCompletadas: Int,
    val tareasEnProgreso: Int,
    val tareasPendientes: Int
)

interface ManageTrabajadorUseCase {
    fun updateTrabajador(id: UUID, nombreCompleto: String, documento: String, tarifaHora: BigDecimal): Trabajador
    fun deleteTrabajador(id: UUID)
    fun getTrabajadorSummary(id: UUID): TrabajadorSummaryResponse
}

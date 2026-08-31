package com.agroflow.reports.infrastructure.adapter.`in`.web

import com.agroflow.finance.application.port.`in`.ManageFinanzasUseCase
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/reports")
class ReportsController(
    private val manageFinanzasUseCase: ManageFinanzasUseCase
) {

    @GetMapping("/finca/{fincaId}/balance-csv")
    fun getBalanceCsv(@PathVariable("fincaId") fincaId: UUID): ResponseEntity<String> {
        val balance = manageFinanzasUseCase.getBalanceFinca(fincaId)

        val csvBuilder = StringBuilder()
        csvBuilder.append("Ingresos Totales, Egresos Totales, Utilidad Neta\n")
        csvBuilder.append("${balance.totalIngresos}, ${balance.totalEgresos}, ${balance.utilidadNeta}\n")
        csvBuilder.append("\n")
        csvBuilder.append("ID, Tipo, Categoria, Monto, Fecha\n")
        
        balance.transacciones.forEach { t ->
            csvBuilder.append("${t.id}, ${t.tipoMovimiento}, ${t.categoria}, ${t.montoTotal}, ${t.fechaTransaccion}\n")
        }

        val headers = HttpHeaders()
        headers.contentType = MediaType.parseMediaType("text/csv")
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"reporte_finca.csv\"")

        return ResponseEntity(csvBuilder.toString(), headers, HttpStatus.OK)
    }
}

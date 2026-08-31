package com.agroflow.lotes.infrastructure.adapter.`in`.web

import com.agroflow.lotes.application.port.`in`.ManageLotesUseCase
import com.agroflow.lotes.domain.model.Lote
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.util.UUID

@RestController
@RequestMapping("/api/lotes")
class LoteController(
    private val manageLotesUseCase: ManageLotesUseCase
) {

    @GetMapping("/finca/{fincaId}")
    fun getLotesByFinca(@PathVariable fincaId: UUID): ResponseEntity<List<Lote>> {
        val lotes = manageLotesUseCase.getLotesByFinca(fincaId)
        return ResponseEntity.ok(lotes)
    }

    @PostMapping
    fun registrarLote(@RequestBody request: LoteRequest): ResponseEntity<Lote> {
        val lote = Lote(
            fincaId = request.fincaId,
            nombre = request.nombre,
            latitud = request.latitud,
            longitud = request.longitud,
            fechaRegistro = request.fechaRegistro ?: LocalDateTime.now(),
            estadoSincronizacion = "SINCRONIZADO"
        )
        val created = manageLotesUseCase.registrarLote(lote)
        return ResponseEntity.ok(created)
    }
}

data class LoteRequest(
    val fincaId: UUID,
    val nombre: String,
    val latitud: Double?,
    val longitud: Double?,
    val fechaRegistro: LocalDateTime? = null
)

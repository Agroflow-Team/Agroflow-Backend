package com.agroflow.personnel.infrastructure.adapter.`in`.web

import com.agroflow.personnel.application.port.`in`.FindTrabajadoresUseCase
import com.agroflow.personnel.domain.model.Trabajador
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/trabajadores")
class TrabajadorController(
    private val findTrabajadoresUseCase: FindTrabajadoresUseCase
) {

    // Consultar todos los trabajadores
    @GetMapping
    fun getAllTrabajadores(): ResponseEntity<List<Trabajador>> {
        val trabajadores = findTrabajadoresUseCase.getAllTrabajadores()
        return ResponseEntity.ok(trabajadores)
    }

    // Consultar trabajadores filtrados por finca (¡Súper útil para la app móvil!)
    @GetMapping("/finca/{fincaId}")
    fun getTrabajadoresByFinca(@PathVariable fincaId: UUID): ResponseEntity<List<Trabajador>> {
        val trabajadores = findTrabajadoresUseCase.getTrabajadoresByFinca(fincaId)
        return ResponseEntity.ok(trabajadores)
    }
}
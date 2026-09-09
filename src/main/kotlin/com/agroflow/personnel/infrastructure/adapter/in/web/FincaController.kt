package com.agroflow.personnel.infrastructure.adapter.`in`.web

import com.agroflow.personnel.application.port.`in`.FindFincasUseCase
import com.agroflow.personnel.domain.model.Finca
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/fincas")
class FincaController(
    private val findFincasUseCase: FindFincasUseCase
) {

    @GetMapping
    fun getAllFincas(): ResponseEntity<List<Finca>> {
        val fincas = findFincasUseCase.getAllFincas()
        return ResponseEntity.ok(fincas)
    }

    @PostMapping
    fun createFinca(@RequestBody request: CreateFincaRequest): ResponseEntity<Finca> {
        val finca = findFincasUseCase.createFinca(request.nombre)
        return ResponseEntity.ok(finca)
    }

    @DeleteMapping("/{id}")
    fun deleteFinca(@PathVariable id: java.util.UUID): ResponseEntity<Any> {
        return try {
            findFincasUseCase.deleteFinca(id)
            ResponseEntity.noContent().build()
        } catch (e: IllegalStateException) {
            ResponseEntity.status(org.springframework.http.HttpStatus.CONFLICT).body(mapOf("error" to e.message))
        }
    }
}

data class CreateFincaRequest(
    val nombre: String
)
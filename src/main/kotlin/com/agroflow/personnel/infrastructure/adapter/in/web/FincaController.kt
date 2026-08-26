package com.agroflow.personnel.infrastructure.adapter.`in`.web

import com.agroflow.personnel.application.port.`in`.FindFincasUseCase
import com.agroflow.personnel.domain.model.Finca
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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
}
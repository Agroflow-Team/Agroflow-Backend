package com.agroflow.personnel.infrastructure.adapter.`in`.web

import com.agroflow.personnel.application.port.`in`.LoginRequest
import com.agroflow.personnel.application.port.`in`.LoginResponse
import com.agroflow.personnel.application.port.`in`.LoginUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val loginUseCase: LoginUseCase
) {

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<LoginResponse> {
        val response = loginUseCase.authenticate(request)
        return ResponseEntity.ok(response)
    }
}
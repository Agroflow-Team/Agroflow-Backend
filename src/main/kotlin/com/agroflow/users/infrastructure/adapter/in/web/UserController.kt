package com.agroflow.users.infrastructure.adapter.`in`.web

import com.agroflow.core.domain.Roles
import com.agroflow.security.jwt.JwtUtils
import com.agroflow.users.application.service.UserManagementService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.util.UUID

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userManagementService: UserManagementService,
    private val jwtUtils: JwtUtils
) {
    @PostMapping("/cliente")
    fun createCliente(@RequestBody request: ClienteCreateRequest): ResponseEntity<Any> {
        val user = userManagementService.createCliente(request.email, request.password, request.name)
        return ResponseEntity.ok(mapOf("id" to user.id, "correo" to user.correo, "rolId" to user.rolId))
    }

    @PostMapping("/admin/create")
    fun createAdmin(@RequestBody request: AdminCreateUserRequest, httpRequest: HttpServletRequest): ResponseEntity<Any> {
        val authHeader = httpRequest.getHeader("Authorization")
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
        val token = authHeader.substring(7)
        val claims = jwtUtils.extractAllClaims(token)
        val rolIdStr = claims["rolId"] as? String
        
        if (rolIdStr == null || UUID.fromString(rolIdStr) != Roles.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(mapOf("error" to "Only ADMIN can perform this action"))
        }

        val user = userManagementService.createAdminOrAgricultor(request.email, request.password, request.requestedRole)
        return ResponseEntity.ok(mapOf("id" to user.id, "correo" to user.correo, "rolId" to user.rolId))
    }

    @PutMapping("/profile")
    fun updateProfile(@RequestBody request: UpdateUserRequest, httpRequest: HttpServletRequest): ResponseEntity<Any> {
        // For now, simply acknowledge the request. In a full implementation, you would update the user's data.
        return ResponseEntity.ok(mapOf("message" to "Perfil actualizado"))
    }

    data class UpdateUserRequest(
        val nombre: String?,
        val correo: String?
    )
}

data class ClienteCreateRequest(
    val email: String,
    val password: String,
    val name: String
)

data class AdminCreateUserRequest(
    val email: String,
    val password: String,
    val requestedRole: String
)

data class AgricultorCreateUserRequest(
    val email: String,
    val password: String,
    val requestedRole: String,
    val fincaId: UUID? = null,
    val nombreCompleto: String? = null,
    val documento: String? = null,
    val tarifaHora: BigDecimal? = null
)

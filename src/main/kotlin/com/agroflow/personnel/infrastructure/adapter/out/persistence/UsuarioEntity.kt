package com.agroflow.personnel.infrastructure.adapter.out.persistence

import com.agroflow.personnel.domain.model.Usuario
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "usuarios")
class UsuarioEntity(
    @Id
    @Column(name = "id")
    val id: UUID? = null,

    @Column(name = "rol_id")
    val rolId: UUID,

    @Column(name = "correo")
    val correo: String,

    @Column(name = "clave_hash")
    val claveHash: String,

    @Column(name = "estado")
    val estado: String,

    @Column(name = "fecha_creacion")
    val fechaCreacion: LocalDateTime = LocalDateTime.now()
) {
    fun toDomain(): Usuario = Usuario(
        id = id,
        rolId = rolId,
        correo = correo,
        estado = estado
    )
}
package com.agroflow.personnel.infrastructure.adapter.out.persistence

import com.agroflow.personnel.domain.model.Finca
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "fincas")
class FincaEntity(
    @Id
    @Column(name = "id")
    val id: UUID? = null,

    @Column(name = "nombre")
    val nombre: String,

    @Column(name = "fecha_registro")
    val fechaRegistro: LocalDateTime
) {
    fun toDomain(): Finca = Finca(
        id = id,
        nombre = nombre,
        fechaRegistro = fechaRegistro
    )
}
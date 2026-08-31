package com.agroflow.personnel.infrastructure.adapter.out.persistence

import com.agroflow.personnel.domain.model.Trabajador
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "perfiles_trabajadores")
class TrabajadorEntity(
    @Id
    @Column(name = "id")
    val id: UUID? = null,

    @Column(name = "usuario_id")
    val usuarioId: UUID,

    @Column(name = "finca_id")
    val fincaId: UUID,

    @Column(name = "nombre_completo")
    val nombreCompleto: String,

    @Column(name = "documento")
    val documento: String,

    @Column(name = "tarifa_hora")
    val tarifaHora: BigDecimal,

    @Column(name = "fecha_registro")
    val fechaRegistro: LocalDateTime
) {
    fun toDomain(): Trabajador = Trabajador(
        id = id,
        usuarioId = usuarioId,
        fincaId = fincaId,
        nombreCompleto = nombreCompleto,
        documento = documento,
        tarifaHora = tarifaHora,
        fechaRegistro = fechaRegistro
    )

    companion object {
        fun fromDomain(domain: Trabajador): TrabajadorEntity = TrabajadorEntity(
            id = domain.id ?: UUID.randomUUID(),
            usuarioId = domain.usuarioId,
            fincaId = domain.fincaId,
            nombreCompleto = domain.nombreCompleto,
            documento = domain.documento,
            tarifaHora = domain.tarifaHora,
            fechaRegistro = domain.fechaRegistro
        )
    }
}
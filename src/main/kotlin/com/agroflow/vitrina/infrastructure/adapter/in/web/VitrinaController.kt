package com.agroflow.vitrina.infrastructure.adapter.`in`.web

import com.agroflow.vitrina.application.port.`in`.ManageVitrinaUseCase
import com.agroflow.vitrina.domain.model.EstadoPublicacion
import com.agroflow.vitrina.domain.model.Publicacion
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@RestController
@RequestMapping("/api/vitrina")
class VitrinaController(
    private val manageVitrinaUseCase: ManageVitrinaUseCase
) {

    @GetMapping
    fun getPublicacionesActivas(): ResponseEntity<List<Publicacion>> {
        val publicaciones = manageVitrinaUseCase.getPublicacionesActivas()
        return ResponseEntity.ok(publicaciones)
    }

    @GetMapping("/finca/{fincaId}")
    fun getPublicacionesByFinca(@PathVariable fincaId: UUID): ResponseEntity<List<Publicacion>> {
        val publicaciones = manageVitrinaUseCase.getPublicacionesByFinca(fincaId)
        return ResponseEntity.ok(publicaciones)
    }

    @PostMapping
    fun crearPublicacion(@RequestBody request: CrearPublicacionRequest): ResponseEntity<Publicacion> {
        val publicacion = Publicacion(
            fincaId = request.fincaId,
            tituloProducto = request.tituloProducto,
            descripcion = request.descripcion,
            precio = request.precio,
            cantidadDisponible = request.cantidadDisponible,
            imagenUrl = request.imagenUrl,
            estadoPublicacion = request.estadoPublicacion ?: EstadoPublicacion.ACTIVA,
            fechaCreacion = request.fechaCreacion ?: LocalDateTime.now(),
            estadoSincronizacion = "SINCRONIZADO"
        )
        val created = manageVitrinaUseCase.crearPublicacion(publicacion)
        return ResponseEntity.ok(created)
    }

    @PutMapping("/{id}")
    fun editarPublicacion(@PathVariable id: UUID, @RequestBody request: EditarPublicacionRequest): ResponseEntity<Publicacion> {
        val publicacionUpdate = Publicacion(
            id = id,
            fincaId = request.fincaId, // assuming fincaId is required or pass existing
            tituloProducto = request.tituloProducto,
            descripcion = request.descripcion,
            precio = request.precio,
            cantidadDisponible = request.cantidadDisponible,
            imagenUrl = request.imagenUrl,
            estadoPublicacion = request.estadoPublicacion ?: EstadoPublicacion.ACTIVA,
            fechaCreacion = LocalDateTime.now(), // not used in update
            estadoSincronizacion = "SINCRONIZADO"
        )
        val updated = manageVitrinaUseCase.editarPublicacion(id, publicacionUpdate)
        return ResponseEntity.ok(updated)
    }

    @PostMapping("/upload-image")
    fun uploadImage(@RequestParam("file") file: org.springframework.web.multipart.MultipartFile): ResponseEntity<Map<String, String>> {
        if (file.isEmpty) {
            return ResponseEntity.badRequest().build()
        }
        try {
            val uploadsDir = java.io.File("uploads")
            if (!uploadsDir.exists()) {
                uploadsDir.mkdir()
            }
            val originalFilename = file.originalFilename ?: "image.jpg"
            val extension = originalFilename.substringAfterLast('.', "jpg")
            val newFilename = "${UUID.randomUUID()}.$extension"
            
            val destFile = java.io.File(uploadsDir, newFilename)
            file.transferTo(destFile)
            
            // The image will be accessible at /uploads/{newFilename}
            val imageUrl = "/uploads/$newFilename"
            return ResponseEntity.ok(mapOf("url" to imageUrl))
        } catch (e: Exception) {
            return ResponseEntity.internalServerError().build()
        }
    }

    @PatchMapping("/{id}/estado")
    fun cambiarEstado(@PathVariable id: UUID, @RequestBody request: CambiarEstadoRequest): ResponseEntity<Publicacion> {
        val updated = manageVitrinaUseCase.cambiarEstado(id, request.estado)
        return ResponseEntity.ok(updated)
    }

    @DeleteMapping("/{id}")
    fun eliminarPublicacion(@PathVariable id: UUID): ResponseEntity<Void> {
        manageVitrinaUseCase.eliminarPublicacion(id)
        return ResponseEntity.noContent().build()
    }
}

data class CrearPublicacionRequest(
    val fincaId: UUID,
    val tituloProducto: String,
    val descripcion: String,
    val precio: BigDecimal,
    val cantidadDisponible: Int,
    val imagenUrl: String?,
    val estadoPublicacion: EstadoPublicacion? = null,
    val fechaCreacion: LocalDateTime? = null
)

data class EditarPublicacionRequest(
    val fincaId: UUID,
    val tituloProducto: String,
    val descripcion: String,
    val precio: BigDecimal,
    val cantidadDisponible: Int,
    val imagenUrl: String?,
    val estadoPublicacion: EstadoPublicacion? = null
)

data class CambiarEstadoRequest(
    val estado: EstadoPublicacion
)

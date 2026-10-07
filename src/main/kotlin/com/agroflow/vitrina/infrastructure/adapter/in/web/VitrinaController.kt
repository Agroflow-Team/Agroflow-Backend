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
    private val manageVitrinaUseCase: ManageVitrinaUseCase,
    @org.springframework.beans.factory.annotation.Value("\${app.upload-dir:uploads}")
    private val uploadDir: String
) {

    companion object {
        private val ALLOWED_EXTENSIONS = setOf("jpg", "jpeg", "png", "webp")
    }

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
    fun crearPublicacion(@RequestBody request: CrearPublicacionRequest): ResponseEntity<Any> {
        return try {
            val fincaIdUuid = request.fincaId?.let { UUID.fromString(it) }
                ?: throw IllegalArgumentException("fincaId is required and must be a valid UUID")
                
            val publicacion = Publicacion(
                fincaId = fincaIdUuid,
                tituloProducto = request.tituloProducto ?: "",
                descripcion = request.descripcion ?: "",
                precio = request.precio ?: BigDecimal.ZERO,
                cantidadDisponible = request.cantidadDisponible ?: 0,
                imagenUrl = request.imagenUrl,
                estadoPublicacion = request.estadoPublicacion ?: EstadoPublicacion.ACTIVA,
                fechaCreacion = request.fechaCreacion ?: LocalDateTime.now(),
                estadoSincronizacion = "SINCRONIZADO"
            )
            val created = manageVitrinaUseCase.crearPublicacion(publicacion)
            ResponseEntity.ok(created)
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(mapOf("error" to (e.message ?: "Invalid request data")))
        }
    }

    @PutMapping("/{id}")
    fun editarPublicacion(@PathVariable id: UUID, @RequestBody request: EditarPublicacionRequest): ResponseEntity<Any> {
        return try {
            val fincaIdUuid = request.fincaId?.let { UUID.fromString(it) }
                ?: throw IllegalArgumentException("fincaId is required and must be a valid UUID")
                
            val publicacionUpdate = Publicacion(
                id = id,
                fincaId = fincaIdUuid,
                tituloProducto = request.tituloProducto ?: "",
                descripcion = request.descripcion ?: "",
                precio = request.precio ?: BigDecimal.ZERO,
                cantidadDisponible = request.cantidadDisponible ?: 0,
                imagenUrl = request.imagenUrl,
                estadoPublicacion = request.estadoPublicacion ?: EstadoPublicacion.ACTIVA,
                fechaCreacion = LocalDateTime.now(),
                estadoSincronizacion = "SINCRONIZADO"
            )
            val updated = manageVitrinaUseCase.editarPublicacion(id, publicacionUpdate)
            ResponseEntity.ok(updated)
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(mapOf("error" to (e.message ?: "Invalid request data")))
        }
    }

    @PostMapping("/upload-image")
    fun uploadImage(@RequestParam("file") file: org.springframework.web.multipart.MultipartFile): ResponseEntity<Any> {
        if (file.isEmpty) {
            return ResponseEntity.badRequest().body(mapOf("error" to "File is empty"))
        }
        try {
            val uploadsDir = java.nio.file.Paths.get(uploadDir).toAbsolutePath().normalize().toFile()
            if (!uploadsDir.exists()) {
                uploadsDir.mkdirs()
            }
            val originalFilename = file.originalFilename ?: "image.jpg"
            val rawExtension = originalFilename.substringAfterLast('.', "jpg").lowercase()
            val extension = if (rawExtension in ALLOWED_EXTENSIONS) rawExtension else "jpg"
            val newFilename = "${UUID.randomUUID()}.$extension"
            
            val destFile = java.io.File(uploadsDir, newFilename)
            file.transferTo(destFile.absoluteFile)
            
            val imageUrl = "/uploads/$newFilename"
            return ResponseEntity.ok(mapOf("url" to imageUrl))
        } catch (e: Exception) {
            e.printStackTrace()
            return ResponseEntity.internalServerError().body(mapOf("error" to (e.message ?: "Unknown error")))
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
    val fincaId: String?,
    val tituloProducto: String?,
    val descripcion: String?,
    val precio: BigDecimal?,
    val cantidadDisponible: Int?,
    val imagenUrl: String?,
    val estadoPublicacion: EstadoPublicacion? = null,
    val fechaCreacion: LocalDateTime? = null
)

data class EditarPublicacionRequest(
    val fincaId: String?,
    val tituloProducto: String?,
    val descripcion: String?,
    val precio: BigDecimal?,
    val cantidadDisponible: Int?,
    val imagenUrl: String?,
    val estadoPublicacion: EstadoPublicacion? = null
)

data class CambiarEstadoRequest(
    val estado: EstadoPublicacion
)

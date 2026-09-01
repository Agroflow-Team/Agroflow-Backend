package com.agroflow

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import com.agroflow.personnel.infrastructure.adapter.out.persistence.SpringDataUsuarioRepository

@SpringBootApplication
class AgroFlowApplication {
    @Bean
    fun fixCarlosPassword(usuarioRepository: SpringDataUsuarioRepository): CommandLineRunner {
        return CommandLineRunner {
            val user = usuarioRepository.findByCorreo("carlos.trabajador@agroflow.com")
            if (user.isPresent) {
                val entity = user.get()
                val updated = com.agroflow.personnel.infrastructure.adapter.out.persistence.UsuarioEntity(
                    id = entity.id,
                    rolId = entity.rolId,
                    correo = entity.correo,
                    claveHash = "123",
                    estado = entity.estado,
                    fechaCreacion = entity.fechaCreacion
                )
                usuarioRepository.save(updated)
            }
        }
    }
}

fun main(args: Array<String>) {
    runApplication<AgroFlowApplication>(*args)
}

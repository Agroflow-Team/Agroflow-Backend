package com.agroflow

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import com.agroflow.personnel.infrastructure.adapter.out.persistence.SpringDataUsuarioRepository

@SpringBootApplication
class AgroFlowApplication {
    @Bean
    fun cleanupDemoTables(jdbcTemplate: org.springframework.jdbc.core.JdbcTemplate): CommandLineRunner {
        return CommandLineRunner {
            try {
                jdbcTemplate.execute("DROP TABLE IF EXISTS demo_inventory CASCADE;")
                jdbcTemplate.execute("DROP TABLE IF EXISTS demo_tasks CASCADE;")
                jdbcTemplate.execute("DROP TABLE IF EXISTS demo_users CASCADE;")
                jdbcTemplate.execute("DROP TABLE IF EXISTS demo_fincas CASCADE;")
                jdbcTemplate.execute("DROP TABLE IF EXISTS demo_roles CASCADE;")
                println(">>> TODAS LAS TABLAS DEMO FUERON ELIMINADAS CON EXITO DE SUPABASE <<<")
            } catch (e: Exception) {
                println(">>> Error limpiando tablas demo: ${e.message} <<<")
            }
        }
    }
}

fun main(args: Array<String>) {
    runApplication<AgroFlowApplication>(*args)
}

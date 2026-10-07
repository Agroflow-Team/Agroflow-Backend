package com.agroflow.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.nio.file.Files
import java.nio.file.Paths

@Configuration
class WebConfig(
    @Value("\${app.upload-dir:uploads}") private val uploadDir: String
) : WebMvcConfigurer {
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        val uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize()
        Files.createDirectories(uploadPath)
        // toUri() genera "file:///home/uploads/" en Linux y "file:///C:/.../uploads/" en Windows
        var location = uploadPath.toUri().toString()
        if (!location.endsWith("/")) location += "/"
        registry.addResourceHandler("/uploads/**")
            .addResourceLocations(location)
    }
}

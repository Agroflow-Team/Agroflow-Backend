package com.agroflow.personnel.application.service

import com.agroflow.personnel.application.port.`in`.FindFincasUseCase
import com.agroflow.personnel.application.port.out.FincaRepositoryPort
import com.agroflow.personnel.application.port.out.TrabajadorRepositoryPort
import com.agroflow.inventory.application.port.out.InventoryRepositoryPort
import com.agroflow.personnel.domain.model.Finca
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class FincaService(
    private val fincaRepository: FincaRepositoryPort,
    private val trabajadorRepository: TrabajadorRepositoryPort,
    private val inventoryRepository: InventoryRepositoryPort
) : FindFincasUseCase {
    override fun getAllFincas(): List<Finca> {
        return fincaRepository.findAll()
    }

    override fun createFinca(nombre: String): Finca {
        val finca = Finca(nombre = nombre)
        return fincaRepository.save(finca)
    }

    override fun deleteFinca(id: UUID) {
    // El usuario confirmó la eliminación, procedemos a borrar.
    // Dependiendo de la base de datos, esto fallará si no hay ON DELETE CASCADE.
    // Para asegurar que borra, borraremos los dependientes directos (trabajadores e inventario).
        val trabajadores = trabajadorRepository.findByFincaId(id)
        trabajadores.forEach { trabajadorRepository.deleteById(it.id!!) }
        
        val inventario = inventoryRepository.findByFincaId(id)
        inventario.forEach { inventoryRepository.deleteById(it.id!!) }

        fincaRepository.deleteById(id)
    }
}
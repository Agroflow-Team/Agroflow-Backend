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
        val hasTrabajadores = trabajadorRepository.findByFincaId(id).isNotEmpty()
        val hasInventory = inventoryRepository.findByFincaId(id).isNotEmpty()

        if (hasTrabajadores || hasInventory) {
            throw IllegalStateException("No se puede eliminar la finca porque tiene trabajadores o inventario asociado.")
        }

        fincaRepository.deleteById(id)
    }
}
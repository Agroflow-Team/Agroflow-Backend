package com.agroflow.personnel.application.service

import com.agroflow.personnel.application.port.`in`.FindTrabajadoresUseCase
import com.agroflow.personnel.application.port.out.TrabajadorRepositoryPort
import com.agroflow.personnel.domain.model.Trabajador
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class TrabajadorService(
    private val trabajadorRepository: TrabajadorRepositoryPort
) : FindTrabajadoresUseCase {

    override fun getAllTrabajadores(): List<Trabajador> {
        return trabajadorRepository.findAll()
    }

    override fun getTrabajadoresByFinca(fincaId: UUID): List<Trabajador> {
        return trabajadorRepository.findByFincaId(fincaId)
    }
}
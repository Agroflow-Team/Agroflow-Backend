package com.agroflow.personnel.application.service

import com.agroflow.personnel.application.port.`in`.FindFincasUseCase
import com.agroflow.personnel.application.port.out.FincaRepositoryPort
import com.agroflow.personnel.domain.model.Finca
import org.springframework.stereotype.Service

@Service
class FincaService(
    private val fincaRepository: FincaRepositoryPort
) : FindFincasUseCase {
    override fun getAllFincas(): List<Finca> {
        return fincaRepository.findAll()
    }
}
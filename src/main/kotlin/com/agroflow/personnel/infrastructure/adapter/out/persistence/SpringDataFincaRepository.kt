package com.agroflow.personnel.infrastructure.adapter.out.persistence

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface SpringDataFincaRepository : JpaRepository<FincaEntity, UUID>

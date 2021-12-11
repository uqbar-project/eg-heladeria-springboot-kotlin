package org.uqbarprojec.heladeriakotlin.dao

import org.uqbarprojec.heladeriakotlin.model.Duenio
import org.uqbarprojec.heladeriakotlin.model.Heladeria
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RepoDuenio : CrudRepository<Duenio, Long> {

    fun findByNombreCompleto(nombreCompleto: String): Duenio

    fun deleteByNombreCompleto(nombreCompleto: String)
}
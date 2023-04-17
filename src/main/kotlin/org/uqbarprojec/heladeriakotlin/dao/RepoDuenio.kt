package org.uqbarprojec.heladeriakotlin.dao

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.uqbarprojec.heladeriakotlin.model.Duenio

@Repository
interface RepoDuenio : CrudRepository<Duenio, Long> {

    fun findByNombreCompleto(nombreCompleto: String): Duenio

    fun deleteByNombreCompleto(nombreCompleto: String)
}
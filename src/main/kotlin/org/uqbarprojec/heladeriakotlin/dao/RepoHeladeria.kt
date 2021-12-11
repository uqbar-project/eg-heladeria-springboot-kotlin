package org.uqbarprojec.heladeriakotlin.dao

import org.uqbarprojec.heladeriakotlin.model.Heladeria
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RepoHeladeria : CrudRepository<Heladeria, Long> {

    fun findByNombreContaining(text: String): List<Heladeria>
}
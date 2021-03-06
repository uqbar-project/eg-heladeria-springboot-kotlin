package org.uqbarprojec.heladeriakotlin.dao

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.uqbarprojec.heladeriakotlin.model.Heladeria
import java.util.Optional

@Repository
interface RepoHeladeria : CrudRepository<Heladeria, Long> {

    @EntityGraph(attributePaths = ["duenio"])
    fun findByNombreContainingIgnoreCase(text: String): List<Heladeria>

    @EntityGraph(attributePaths = ["duenio", "gustos"])
    override fun findById(id: Long): Optional<Heladeria>
}

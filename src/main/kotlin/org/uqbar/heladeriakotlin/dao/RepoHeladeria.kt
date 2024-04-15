package org.uqbar.heladeriakotlin.dao

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.uqbar.heladeriakotlin.model.Heladeria
import java.util.*

@Repository
interface RepoHeladeria : CrudRepository<Heladeria, Long> {

    @EntityGraph(attributePaths = ["duenio"])
    fun findByNombreContainingIgnoreCase(text: String): List<Heladeria>

    @EntityGraph(attributePaths = ["duenio", "gustos"], type = EntityGraph.EntityGraphType.LOAD)
    override fun findById(id: Long): Optional<Heladeria>
}

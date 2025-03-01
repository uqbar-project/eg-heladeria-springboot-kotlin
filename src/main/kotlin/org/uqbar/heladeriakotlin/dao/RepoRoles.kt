package org.uqbar.heladeriakotlin.dao

import org.springframework.data.repository.CrudRepository
import org.uqbar.heladeriakotlin.model.Rol
import java.util.*

interface RepoRoles : CrudRepository<Rol, Long> {
   fun findByName(roleName: String): Optional<Rol>
}
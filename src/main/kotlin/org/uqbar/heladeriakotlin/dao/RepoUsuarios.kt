package org.uqbar.heladeriakotlin.dao

import org.springframework.data.repository.CrudRepository
import org.uqbar.heladeriakotlin.model.Usuario
import java.util.*

interface RepoUsuarios : CrudRepository<Usuario, Long> {
   fun findByUsername(username: String): Optional<Usuario>
}
package org.uqbar.heladeriakotlin.dao

import org.springframework.data.repository.CrudRepository
import org.uqbar.heladeriakotlin.model.RefreshToken
import java.util.*

interface RepoRefreshToken : CrudRepository<RefreshToken, Long> {
    fun findByToken(token: String): Optional<RefreshToken>
    fun findByUsername(username: String): List<RefreshToken>
}

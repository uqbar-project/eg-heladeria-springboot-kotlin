package org.uqbar.heladeriakotlin.dao

import org.springframework.data.repository.CrudRepository
import org.uqbar.heladeriakotlin.model.RefreshToken
import java.util.*

interface RepoRefreshToken : CrudRepository<RefreshToken, Long> {
    fun findByTokenHash(tokenHash: String): Optional<RefreshToken>
}

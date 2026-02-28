package org.uqbar.heladeriakotlin.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "refresh_tokens")
class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(unique = true, nullable = false)
    var token: String = ""

    @Column(nullable = false)
    var username: String = ""

    @Column(nullable = false)
    var expirationDate: LocalDateTime = LocalDateTime.now()

    var revoked: Boolean = false

    fun isValid(): Boolean = !revoked && LocalDateTime.now().isBefore(expirationDate)
}

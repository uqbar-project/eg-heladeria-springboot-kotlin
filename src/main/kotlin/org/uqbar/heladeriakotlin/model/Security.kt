package org.uqbar.heladeriakotlin.model


import jakarta.persistence.*
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.uqbar.heladeriakotlin.errorHandling.CredencialesInvalidasException

@Entity
@Table(name = "roles")
class Rol(
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    var name: String = ""
}

@Entity
@Table(name = "users")
class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    var username: String = ""
    var email: String = ""
    private var password: String = ""

    @ManyToMany
    @JoinTable(
        name = "user_roles",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")]
    )
    val roles: MutableSet<Rol> = mutableSetOf()

    // https://www.baeldung.com/java-password-hashing
    fun validarCredenciales(passwordAVerificar: String) {
        if (!getDefaultEncoder().matches(passwordAVerificar, password)) {
            throw CredencialesInvalidasException()
        }
    }

    private fun getDefaultEncoder(): PasswordEncoder {
        return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8()!!
    }

    fun agregarRol(rol: Rol) {
        roles.add(rol)
    }

    fun crearPassword(rawPassword: String) {
        password = getDefaultEncoder().encode(rawPassword)
    }

    fun buildUser() = User(username, password, roles.map { SimpleGrantedAuthority(it.name) })

}

enum class ROLES(val roleName: String) {
    ADMIN("ADMIN"), READONLY("READONLY")
}
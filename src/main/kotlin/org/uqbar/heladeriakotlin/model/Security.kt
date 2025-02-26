package org.uqbar.heladeriakotlin.model


import jakarta.persistence.*
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.uqbar.heladeriakotlin.errorHandling.BusinessException
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

const val longitudMinimaPassword = 3

@Entity
@Table(name = "users")
class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    var username: String = ""
    var email: String = ""
    var password: String = ""

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

    fun validar() {
        if (username.trim().isBlank()) {
            throw BusinessException("Debe ingresar nombre de usuario")
        }
        if (password.trim().isBlank()) {
            throw BusinessException("Debe ingresar contraseña")
        }
        if (password.length < longitudMinimaPassword) {
            throw BusinessException("La contraseña debe tener al menos $longitudMinimaPassword caracteres")
        }
    }

    fun crearPassword(rawPassword: String) {
        password = getDefaultEncoder().encode(rawPassword)
    }

}


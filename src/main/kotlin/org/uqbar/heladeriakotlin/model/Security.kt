package org.uqbar.heladeriakotlin.model


import jakarta.persistence.*;

@Entity
@Table(name = "roles")
data class Rol(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val name: String
)

@Entity
@Table(name = "users")
data class Usuario(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val name: String,
    val username: String,
    val email: String,
    val password: String,
) {
    @ManyToMany(cascade = [(CascadeType.ALL)])
    @JoinTable(
        name = "user_roles",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")]
    )
    val roles: MutableSet<Rol> = mutableSetOf()
}


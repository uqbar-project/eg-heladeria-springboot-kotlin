package org.uqbarprojec.heladeriakotlin.model

import org.uqbarprojec.heladeriakotlin.service.UserException
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Duenio(val nombreCompleto: String) {
    @Id
    @GeneratedValue
    val id: Long = 0

    fun validar() {
        if (nombreCompleto.trim().isEmpty()) {
            throw UserException("El nombre para el dueño no puede ser vacío")
        }
    }
}
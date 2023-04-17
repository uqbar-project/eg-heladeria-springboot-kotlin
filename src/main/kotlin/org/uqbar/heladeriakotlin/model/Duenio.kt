package org.uqbar.heladeriakotlin.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.uqbar.heladeriakotlin.service.UserException

@Entity
class Duenio(val nombreCompleto: String) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    fun validar() {
        if (nombreCompleto.trim().isEmpty()) {
            throw UserException("El nombre para el dueño no puede ser vacío")
        }
    }
}
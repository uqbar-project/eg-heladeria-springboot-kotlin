package org.uqbar.heladeriakotlin.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.uqbar.heladeriakotlin.errorHandling.BusinessException

@Entity
class Duenio(
    val nombreCompleto: String,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
) {

    fun validar() {
        if (nombreCompleto.trim().isEmpty()) {
            throw BusinessException("El nombre de un dueño no puede estar vacio")
        }
    }
}
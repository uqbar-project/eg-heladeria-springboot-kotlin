package org.uqbarprojec.heladeriakotlin.model

import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter
import org.uqbarprojec.heladeriakotlin.service.UserException
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Duenio(val nombreCompleto: String) {
    @Id
    @GeneratedValue(generator = "sequence-generator")
    @GenericGenerator(
        name = "sequence-generator",
        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
        parameters = [Parameter(name = "sequence_name", value = "duenio_sequence"),
            Parameter(name = "initial_value", value = "1"),
            Parameter(name = "increment_size", value = "1")]
    )
    val id: Long = 0

    fun validar() {
        if (nombreCompleto.trim().isEmpty()) {
            throw UserException("El nombre para el dueño no puede ser vacío")
        }
    }
}
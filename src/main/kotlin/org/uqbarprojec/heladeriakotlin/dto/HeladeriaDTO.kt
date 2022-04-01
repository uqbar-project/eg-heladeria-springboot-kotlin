package org.uqbarprojec.heladeriakotlin.dto

import org.uqbarprojec.heladeriakotlin.model.Duenio
import org.uqbarprojec.heladeriakotlin.model.Heladeria
import org.uqbarprojec.heladeriakotlin.model.TipoHeladeria

class HeladeriaDTO(
    val id: Long,
    val nombre: String,
    val tipoHeladeria: TipoHeladeria,
    val duenio: Duenio,
) {

    companion object {
        fun fromHeladeria(heladeria: Heladeria): HeladeriaDTO {
            return HeladeriaDTO(
                id = heladeria.id,
                nombre = heladeria.nombre,
                tipoHeladeria = heladeria.tipoHeladeria,
                duenio = heladeria.duenio
            )
        }
    }

}
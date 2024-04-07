package org.uqbar.heladeriakotlin.dto

import org.uqbar.heladeriakotlin.model.Duenio
import org.uqbar.heladeriakotlin.model.Heladeria
import org.uqbar.heladeriakotlin.model.TipoHeladeria

class HeladeriaDTO(
    val id: Long? = null,
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
package org.uqbarprojec.heladeriakotlin.dto

import org.uqbarprojec.heladeriakotlin.model.Heladeria
import org.uqbarprojec.heladeriakotlin.model.TipoHeladeria

class ActualizarHeladeriaDTO(
    val nombre: String? = null,
    val tipoHeladeria: TipoHeladeria? = null,
    val duenio: DuenioDTO? = null
) {
    fun aplicarCambiosA(heladeria: Heladeria) {
        heladeria.let {
            it.nombre = nombre ?: it.nombre
            it.tipoHeladeria = tipoHeladeria ?: it.tipoHeladeria
        }
    }
}

data class DuenioDTO(val id: Long? = null)

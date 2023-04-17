package org.uqbar.heladeriakotlin.dto

import org.uqbar.heladeriakotlin.model.Heladeria
import org.uqbar.heladeriakotlin.model.TipoHeladeria

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

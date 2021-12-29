package org.uqbarprojec.heladeriakotlin.model

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
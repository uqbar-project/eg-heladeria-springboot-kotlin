package org.uqbar.heladeriakotlin.model

import jakarta.persistence.*
import org.uqbar.heladeriakotlin.errorHandling.BusinessException

@Entity
open class Heladeria(
    var nombre: String,
    @Enumerated(EnumType.STRING) open var tipoHeladeria: TipoHeladeria,
    @ManyToOne(fetch = FetchType.LAZY) open var duenio: Duenio,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
) {

    @ElementCollection(fetch = FetchType.LAZY)
    @MapKeyColumn(name = "gusto")
    @Column(name = "dificultad")
    @CollectionTable(name = "heladeria_gustos", joinColumns = [JoinColumn(name = "heladeria_id")])
    var gustos: MutableMap<String, Int> = mutableMapOf()

    fun validar() {
        if (nombre.isBlank()) {
            throw BusinessException("El nombre de una heladería no puede estar vacío")
        }
        validarGustos(gustos)
    }

    fun validarGusto(gusto: String, dificultad: Int) {
        if (gusto.trim().isEmpty()) {
            throw BusinessException("Un gusto debe tener un nombre no vacío")
        }
        if (dificultad < 1 || dificultad > 10) {
            throw BusinessException("La dificultad debe ser un valor entre 1 y 10 ")
        }
    }

    fun validarGustos(gustos: MutableMap<String, Int>) {
        if (gustos.isEmpty()) {
            throw BusinessException("Una heladería debe tener al menos un gusto asociado")
        }
        gustos.forEach { (gusto, dificultad) -> validarGusto(gusto, dificultad) }
    }

}

enum class TipoHeladeria {
    ECONOMICA, ARTESANAL, INDUSTRIAL
}
package org.uqbar.heladeriakotlin.model

import jakarta.persistence.*
import org.uqbar.heladeriakotlin.service.UserException
import java.text.Normalizer

@Entity
class Heladeria(
    var nombre: String,
    @Enumerated(EnumType.ORDINAL)
    var tipoHeladeria: TipoHeladeria,
    @ManyToOne(fetch = FetchType.LAZY)
    var duenio: Duenio,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    // TODO: quitar el fetch eager y hacerlo lazy
    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name = "gusto")
    @Column(name = "dificultad")
    @CollectionTable(name = "heladeria_gustos", joinColumns = [JoinColumn(name = "heladeria_id")])
    var gustos: MutableMap<String, Int> = mutableMapOf()

    fun validar() {
        if (nombre.isBlank()) {
            throw UserException("Debe cargar el nombre")
        }
        validarGustos(gustos)
    }

    fun agregarGustos(gustosNuevos: MutableMap<String, Int>) {
        validarGustos(gustosNuevos)
        gustosNuevos.forEach { agregarGusto(it.key, it.value) }
    }

    fun agregarGusto(gusto: String, dificultad: Int) {
        val gustoNormalizado = Normalizer.normalize(gusto.lowercase(), Normalizer.Form.NFD).replace("\\p{M}", "")
        gustos[gustoNormalizado] = dificultad
    }

    fun validarGusto(gusto: String, dificultad: Int) {
        if (gusto.trim().isEmpty()) {
            throw UserException("El gusto no puede estar vacío")
        }
        if (dificultad < 1 || dificultad > 10) {
            throw UserException("La dificultad debe ser un valor entre 1 y 10 ")
        }
    }

    fun validarGustos(gustos: MutableMap<String, Int>) {
        if (gustos.isEmpty()) {
            throw UserException("Debe seleccionar al menos un gusto")
        }
        gustos.forEach { (gusto, dificultad) -> validarGusto(gusto, dificultad) }
    }

    fun eliminarGusto(gusto: String) {
        if (!gustos.containsKey(gusto)) {
            throw UserException("El gusto a eliminar '$gusto' no existe")
        }
        gustos.remove(gusto)
    }

}

enum class TipoHeladeria {
    ECONOMICA,
    ARTESANAL,
    INDUSTRIAL
}
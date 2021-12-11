package org.uqbarprojec.heladeriakotlin

import org.uqbarprojec.heladeriakotlin.dao.RepoHeladeria
import org.uqbarprojec.heladeriakotlin.model.Duenio
import org.uqbarprojec.heladeriakotlin.model.Heladeria
import org.uqbarprojec.heladeriakotlin.model.TipoHeladeria
import org.uqbarprojec.heladeriakotlin.service.HeladeriaService
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component

@Component
class Bootstrap(
    private val heladeriaService: HeladeriaService,
    private val repoHeladeria: RepoHeladeria
) : InitializingBean {

    override fun afterPropertiesSet() {
        if (repoHeladeria.count() < 3) {
            initHeladerias()
        }
    }

    fun initHeladerias() {
        val tucan = Heladeria(
            "Tucán",
            TipoHeladeria.ECONOMICA,
            Duenio("Carlos Martinelli"),
        )
        tucan.gustos = mutableMapOf("frutilla" to 3, "maracuya" to 2, "dulce de leche" to 4, "pistacchio" to 6)

        val monteOlivia = Heladeria(
            "Monte Olivia",
            TipoHeladeria.ARTESANAL,
            Duenio("Olivia Heladette"),

            )
        monteOlivia.gustos = mutableMapOf(
            "chocolate amargo" to 8, "dulce de leche" to 3, "mousse de limón" to 5, "crema tramontana" to 9,
            "vainilla" to 1
        )

        val frigor = Heladeria(
            "Frigor",
            TipoHeladeria.INDUSTRIAL,
            Duenio("Manuela Fritzler y Carlos Gorriti"),
        )
        frigor.gustos = mutableMapOf("crema americana" to 2)



        crearOActualizarHeladeria(tucan)
        crearOActualizarHeladeria(monteOlivia)
        crearOActualizarHeladeria(frigor)
    }

    fun crearOActualizarHeladeria(heladeria: Heladeria) {
        repoHeladeria.save(heladeria)
        println("Heladería " + heladeria.nombre + " creada")
    }

}
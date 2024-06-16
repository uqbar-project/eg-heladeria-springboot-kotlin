package org.uqbar.heladeriakotlin

import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component
import org.uqbar.heladeriakotlin.dao.RepoDuenio
import org.uqbar.heladeriakotlin.dao.RepoHeladeria
import org.uqbar.heladeriakotlin.model.Duenio
import org.uqbar.heladeriakotlin.model.Heladeria
import org.uqbar.heladeriakotlin.model.TipoHeladeria

@Component
class Bootstrap(
    private val repoHeladeria: RepoHeladeria,
    private val repoDuenio: RepoDuenio
) : InitializingBean {

    val carlosMartinelli = Duenio("Carlos Martinelli")
    val oliviaHeladette = Duenio("Olivia Heladette")
    val manuelaFritzler = Duenio("Manuela Fritzler y Carlos Gorriti")

    override fun afterPropertiesSet() {
        if (repoHeladeria.count() === 0L) {
            initDuenios()
            initHeladerias()
        }
    }

    fun initDuenios() {
        repoDuenio.run {
            save(carlosMartinelli)
            save(oliviaHeladette)
            save(manuelaFritzler)
        }
    }

    fun initHeladerias() {
        val tucan = Heladeria(
            "Tucán",
            TipoHeladeria.ECONOMICA,
            carlosMartinelli,
        )
        tucan.gustos =
            mutableMapOf(
                "frutilla" to 3,
                "maracuya" to 2,
                "dulce de leche" to 4,
                "pistacchio" to 6,
                "chocolate" to 4,
                "tramontana" to 6,
                "sambayón" to 7,
            )

        val monteOlivia = Heladeria(
            "Monte Olivia",
            TipoHeladeria.ARTESANAL,
            oliviaHeladette

        )
        monteOlivia.gustos = mutableMapOf(
            "chocolate amargo" to 8, "dulce de leche" to 3, "mousse de limón" to 5, "crema tramontana" to 9,
            "vainilla" to 1
        )

        val frigor = Heladeria(
            "Frigor",
            TipoHeladeria.INDUSTRIAL,
            manuelaFritzler,
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
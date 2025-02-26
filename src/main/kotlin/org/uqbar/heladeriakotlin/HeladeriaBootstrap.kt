package org.uqbar.heladeriakotlin

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component
import org.uqbar.heladeriakotlin.dao.RepoDuenio
import org.uqbar.heladeriakotlin.dao.RepoHeladeria
import org.uqbar.heladeriakotlin.dao.RepoRoles
import org.uqbar.heladeriakotlin.dao.RepoUsuarios
import org.uqbar.heladeriakotlin.model.*

@Component
class HeladeriaBootstrap(
    private val repoHeladeria: RepoHeladeria,
    private val repoDuenio: RepoDuenio,
    private val repoUsuarios: RepoUsuarios,
    private val repoRoles: RepoRoles
) : InitializingBean {

    val carlosMartinelli = Duenio("Carlos Martinelli")
    val oliviaHeladette = Duenio("Olivia Heladette")
    val manuelaFritzler = Duenio("Manuela Fritzler y Carlos Gorriti")

    val logger: Logger = LoggerFactory.getLogger(HeladeriaBootstrap::class.java)

    override fun afterPropertiesSet() {
        if (repoHeladeria.count() == 0L) {
            initUsuarios()
            initDuenios()
            initHeladerias()
        }
    }

    fun initUsuarios() {
        val admin = crearRolSiNoExiste("admin")
        val readonly = crearRolSiNoExiste("readonly")
        crearUsuarioSiNoExiste("dodain", "1234", admin)
        crearUsuarioSiNoExiste("viotti", "elnico", admin)
        crearUsuarioSiNoExiste("phm", "phm", readonly)
    }

    fun crearRolSiNoExiste(roleName: String): Rol {
        val role = repoRoles.findByName(roleName)
        if (role.isEmpty) {
            logger.info("Creando rol $roleName")
            return repoRoles.save(Rol().apply {
                name = roleName
            })
        } else {
            logger.info("Rol $roleName ya existe")
            return role.get()
        }
    }

    fun crearUsuarioSiNoExiste(user: String, password: String, role: Rol) {
        if (repoUsuarios.findByUsername(user).isEmpty) {
            logger.info("Creando usuario $user")
            repoUsuarios.save(Usuario().apply {
                username = user
                crearPassword(password)
                agregarRol(role)
            })
        } else {
            logger.info("Usuario $user ya existe")
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
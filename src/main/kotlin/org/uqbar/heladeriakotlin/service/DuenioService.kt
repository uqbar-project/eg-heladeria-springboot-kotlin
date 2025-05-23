package org.uqbar.heladeriakotlin.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.uqbar.heladeriakotlin.dao.RepoDuenio
import org.uqbar.heladeriakotlin.errorHandling.NotFoundException
import org.uqbar.heladeriakotlin.model.Duenio

@Service
class DuenioService {

    @Autowired
    lateinit var repoDuenio: RepoDuenio

    fun findAll(): List<Duenio> {
        return repoDuenio.findAll().toList()
    }

    fun findById(duenioId: Long): Duenio {
        return repoDuenio.findById(duenioId)
            .orElseThrow { throw NotFoundException("No se encontró el duenio indicado: $duenioId") }
    }

    fun validarYGuardar(duenio: Duenio): Duenio {
        duenio.validar()
        return repoDuenio.save(duenio)
    }
}
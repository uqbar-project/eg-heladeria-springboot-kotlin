package org.uqbarprojec.heladeriakotlin.service

import org.uqbarprojec.heladeriakotlin.dao.RepoDuenio
import org.uqbarprojec.heladeriakotlin.model.Duenio
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DuenioService(private val repoDuenio: RepoDuenio) {

    fun findAll() {
        repoDuenio.findAll().toList()
    }

    fun findById(duenioId: Long): Duenio {
        return repoDuenio.findById(duenioId)
            .orElseThrow { throw NotFoundException("No se encontró el duenio indicado: $duenioId") }
    }

    fun validarYGuardar(duenio: Duenio) {
        duenio.validar()
        repoDuenio.save(duenio)
    }

    @Transactional
    fun delete(nombreCompleto: String) {
        repoDuenio.deleteByNombreCompleto(nombreCompleto)
    }
}
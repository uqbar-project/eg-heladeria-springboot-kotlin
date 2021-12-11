package org.uqbarprojec.heladeriakotlin.service

import org.uqbarprojec.heladeriakotlin.dao.RepoHeladeria
import org.uqbarprojec.heladeriakotlin.model.Heladeria
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class HeladeriaService(private val repoHeladeria: RepoHeladeria, private val duenioService: DuenioService) {

    fun findAll() {
        repoHeladeria.findAll().toList()
    }

    fun findByNombre(nombre: String): List<Heladeria> {
        return repoHeladeria.findByNombreContaining(nombre)
    }

    fun findById(heladeriaId: Long): Heladeria {
        return repoHeladeria.findById(heladeriaId)
            .orElseThrow { throw NotFoundException("No se encontró la heladería indicada: $heladeriaId") }
    }

    fun validarYGuardar(heladeria: Heladeria): Heladeria {
        heladeria.validar()
        return repoHeladeria.save(heladeria)
    }

    @Transactional
    fun actualizar(heladeriaId: Long, heladeria: Heladeria) {
        val heladeriaFound: Heladeria = findById(heladeriaId)
        heladeria.duenio = duenioService.findById(heladeria.duenio.id)
        heladeriaFound.merge(heladeria)
        validarYGuardar(heladeriaFound)
    }

    @Transactional
    fun agregarGustos(heladeriaId: Long, gustos: MutableMap<String, Int>) {
        val heladeria: Heladeria = findById(heladeriaId)
        heladeria.agregarGustos(gustos)
        validarYGuardar(heladeria)
    }

    @Transactional
    fun eliminarGustos(heladeriaId: Long, gustos: MutableMap<String, Int>) {
        val heladeria: Heladeria = findById(heladeriaId)
        gustos.forEach { (gusto, _) -> heladeria.eliminarGusto(gusto) }
        validarYGuardar(heladeria)
    }


}
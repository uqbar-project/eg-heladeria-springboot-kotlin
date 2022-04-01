package org.uqbarprojec.heladeriakotlin.service

import org.springframework.stereotype.Service
import org.uqbarprojec.heladeriakotlin.dao.RepoHeladeria
import org.uqbarprojec.heladeriakotlin.dto.ActualizarHeladeriaDTO
import org.uqbarprojec.heladeriakotlin.model.Heladeria

@Service
class HeladeriaService(private val repoHeladeria: RepoHeladeria, private val duenioService: DuenioService) {

    fun findAll(): List<Heladeria> {
        return repoHeladeria.findAll().toList()
    }

    fun findByNombre(nombre: String): List<Heladeria> {
        return repoHeladeria.findByNombreContainingIgnoreCase(nombre)
    }

    fun findById(heladeriaId: Long): Heladeria {
        return repoHeladeria.findById(heladeriaId)
            .orElseThrow { throw NotFoundException("No se encontró la heladería indicada: $heladeriaId") }
    }

    fun validarYGuardar(heladeria: Heladeria): Heladeria {
        heladeria.validar()
        return repoHeladeria.save(heladeria)
    }

    fun actualizar(heladeriaId: Long, heladeriaRequest: ActualizarHeladeriaDTO): Heladeria {
        val heladeriaFound: Heladeria = findById(heladeriaId)
        if (heladeriaRequest.duenio?.id != null) {
            heladeriaFound.duenio = duenioService.findById(heladeriaRequest.duenio.id)
        }
        heladeriaRequest.aplicarCambiosA(heladeriaFound)
        return validarYGuardar(heladeriaFound)
    }

    fun agregarGustos(heladeriaId: Long, gustos: MutableMap<String, Int>): Heladeria {
        val heladeria: Heladeria = findById(heladeriaId)
        heladeria.agregarGustos(gustos)
        return validarYGuardar(heladeria)
    }

    fun eliminarGustos(heladeriaId: Long, gustos: MutableMap<String, Int>): Heladeria {
        val heladeria: Heladeria = findById(heladeriaId)
        gustos.forEach { (gusto, _) -> heladeria.eliminarGusto(gusto) }
        return validarYGuardar(heladeria)
    }


}
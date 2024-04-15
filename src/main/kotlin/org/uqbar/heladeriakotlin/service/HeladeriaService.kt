package org.uqbar.heladeriakotlin.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.uqbar.heladeriakotlin.dao.RepoHeladeria
import org.uqbar.heladeriakotlin.model.Heladeria

@Service
class HeladeriaService {

    @Autowired
    lateinit var repoHeladeria: RepoHeladeria

    @Autowired
    lateinit var duenioService: DuenioService

    fun findAll(): List<Heladeria> {
        return repoHeladeria.findAll().toList()
    }

    fun findByNombre(nombre: String): List<Heladeria> {
        return repoHeladeria.findByNombreContainingIgnoreCase(nombre)
    }

    fun findById(id: Long): Heladeria {
        return repoHeladeria.findById(id)
            .orElseThrow { throw NotFoundException("No se encontró la heladería indicada: $id") }
    }

    fun actualizar(heladeriaId: Long, heladeriaRequest: Heladeria): Heladeria {
        val duenioId = heladeriaRequest.duenio.id
        if (duenioId === null) throw UserException("El id del dueño no puede ser nulo")

        findById(heladeriaId) // Si no existe tira una excepción
        heladeriaRequest.duenio = duenioService.findById(duenioId)
        heladeriaRequest.validar()

        return repoHeladeria.save(heladeriaRequest)
    }

}
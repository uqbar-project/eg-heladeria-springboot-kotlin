package org.uqbar.heladeriakotlin.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.uqbar.heladeriakotlin.dto.HeladeriaDTO
import org.uqbar.heladeriakotlin.model.Duenio
import org.uqbar.heladeriakotlin.model.Heladeria
import org.uqbar.heladeriakotlin.service.DuenioService
import org.uqbar.heladeriakotlin.service.HeladeriaService
import org.uqbar.heladeriakotlin.service.UserException

@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
@RestController
class HeladeriaController {

    @Autowired
    lateinit var heladeriaService: HeladeriaService

    @Autowired
    lateinit var duenioService: DuenioService

    @GetMapping("/heladerias/buscar")
    fun getHeladerias(@RequestParam(required = false) nombre: String?): List<HeladeriaDTO> {
        if (nombre === null) return heladeriaService.findAll().map { HeladeriaDTO.fromHeladeria(it) }
        return heladeriaService.findByNombre(nombre).map { HeladeriaDTO.fromHeladeria(it) }.sortedBy { it.nombre }
    }

    @GetMapping("/heladerias/{id}")
    fun getHeladeria(@PathVariable id: Long): Heladeria {
        return heladeriaService.findById(id)
    }

    @GetMapping("/duenios")
    fun getDuenios(): List<Duenio> {
        return duenioService.findAll()
    }

    @PostMapping("/duenios")
    fun crearDuenio(@RequestBody duenio: Duenio): Duenio {
        return duenioService.validarYGuardar(duenio)
    }

    @PutMapping("/heladerias/{heladeriaId}")
    fun actualizarHeladeria(
        @RequestBody heladeria: Heladeria,
        @PathVariable heladeriaId: Long
    ): Heladeria {
        if (heladeria.id === null) throw UserException("El id de la heladería no puede ser nulo")
        if (heladeria.id != heladeriaId) throw UserException(
            "El id recibido en el body no coincide con el id recibido en la URL"
        )
        return heladeriaService.actualizar(heladeriaId, heladeria)
    }

}

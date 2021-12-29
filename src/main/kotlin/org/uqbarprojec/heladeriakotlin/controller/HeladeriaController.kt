package org.uqbarprojec.heladeriakotlin.controller

import org.springframework.web.bind.annotation.*
import org.uqbarprojec.heladeriakotlin.model.Duenio
import org.uqbarprojec.heladeriakotlin.model.Heladeria
import org.uqbarprojec.heladeriakotlin.model.HeladeriaDTO
import org.uqbarprojec.heladeriakotlin.service.DuenioService
import org.uqbarprojec.heladeriakotlin.service.HeladeriaService

@CrossOrigin
@RestController
class HeladeriaController(private val heladeriaService: HeladeriaService, private val duenioService: DuenioService) {
    @GetMapping("/heladerias/buscar")
    fun getHeladeria(): List<HeladeriaDTO> {
        return heladeriaService.findAll().map { HeladeriaDTO.fromHeladeria(it) }
    }

    @GetMapping("/heladerias/buscar/{text}")
    fun getHeladeria(@PathVariable text: String): List<HeladeriaDTO> {
        return heladeriaService.findByNombre(text).map { HeladeriaDTO.fromHeladeria(it) }
    }

    @GetMapping("/heladerias/id/{id}")
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

    @PatchMapping("/heladerias/{heladeriaId}")
    fun actualizarHeladeria(@RequestBody heladeria: Heladeria, @PathVariable heladeriaId: Long): Heladeria {
        return heladeriaService.actualizar(heladeriaId, heladeria)
    }

    @PostMapping("/heladerias/{heladeriaId}/gustos")
    fun agregarGustos(@RequestBody gustos: MutableMap<String, Int>, @PathVariable heladeriaId: Long): Heladeria {
        return heladeriaService.agregarGustos(heladeriaId, gustos)
    }

    @DeleteMapping("/heladerias/{heladeriaId}/gustos")
    fun eliminarGustos(@RequestBody gustos: MutableMap<String, Int>, @PathVariable heladeriaId: Long): Heladeria {
        return heladeriaService.eliminarGustos(heladeriaId, gustos)
    }
}

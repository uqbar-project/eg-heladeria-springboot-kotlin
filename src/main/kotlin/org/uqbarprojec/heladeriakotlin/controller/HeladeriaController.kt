package org.uqbarprojec.heladeriakotlin.controller

import io.swagger.v3.oas.annotations.Operation
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
    @Operation(summary = "Devuelve la lista de todas las heladerías")
    fun getHeladerias(): List<HeladeriaDTO> {
        return heladeriaService.findAll().map { HeladeriaDTO.fromHeladeria(it) }
    }

    @GetMapping("/heladerias/buscar/{text}")
    @Operation(summary = "Devuelve la información de heladerías buscando un nombre que contenga el valor (sin distinguir mayúsculas de minúsculas)")
    fun getHeladeria(@PathVariable text: String): List<HeladeriaDTO> {
        return heladeriaService.findByNombre(text).map { HeladeriaDTO.fromHeladeria(it) }
    }

    @GetMapping("/heladerias/id/{id}")
    @Operation(summary = "Devuelve la información de una heladería en base a su identificador")
    fun getHeladeria(@PathVariable id: Long): Heladeria {
        return heladeriaService.findById(id)
    }

    @GetMapping("/duenios")
    @Operation(summary = "Devuelve la lista de todos los dueños de heladerías")
    fun getDuenios(): List<Duenio> {
        return duenioService.findAll()
    }

    @PostMapping("/duenios")
    @Operation(summary = "Permite crear un dueño de una heladería")
    fun crearDuenio(@RequestBody duenio: Duenio): Duenio {
        return duenioService.validarYGuardar(duenio)
    }

    @PatchMapping("/heladerias/{heladeriaId}")
    @Operation(summary = "Actualiza la información de una heladería")
    fun actualizarHeladeria(@RequestBody heladeria: Heladeria, @PathVariable heladeriaId: Long): Heladeria {
        return heladeriaService.actualizar(heladeriaId, heladeria)
    }

    @PostMapping("/heladerias/{heladeriaId}/gustos")
    @Operation(summary = "Agrega un gusto nuevo y lo asocia a una heladería")
    fun agregarGustos(@RequestBody gustos: MutableMap<String, Int>, @PathVariable heladeriaId: Long): Heladeria {
        return heladeriaService.agregarGustos(heladeriaId, gustos)
    }

    @DeleteMapping("/heladerias/{heladeriaId}/gustos")
    @Operation(summary = "Elimina una heladería")
    fun eliminarGustos(@RequestBody gustos: MutableMap<String, Int>, @PathVariable heladeriaId: Long): Heladeria {
        return heladeriaService.eliminarGustos(heladeriaId, gustos)
    }

}

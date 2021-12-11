package org.uqbarprojec.heladeriakotlin.controller

import org.uqbarprojec.heladeriakotlin.model.Heladeria
import org.uqbarprojec.heladeriakotlin.service.HeladeriaService
import org.springframework.web.bind.annotation.*

@RestController
class HeladeriaController(private val heladeriaService: HeladeriaService) {
    @GetMapping("/heladeria/{text}")
    fun getHeladeria(@PathVariable text: String): List<Heladeria> {
        return heladeriaService.findByNombre(text)
    }

    @PostMapping("/crear")
    fun createHeladeria(@RequestBody heladeria: Heladeria): Heladeria {
        return heladeriaService.validarYGuardar(heladeria)
    }
}

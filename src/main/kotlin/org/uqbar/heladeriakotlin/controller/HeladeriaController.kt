package org.uqbar.heladeriakotlin.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.uqbar.heladeriakotlin.dto.ActualizarHeladeriaDTO
import org.uqbar.heladeriakotlin.dto.HeladeriaDTO
import org.uqbar.heladeriakotlin.model.Duenio
import org.uqbar.heladeriakotlin.model.Heladeria
import org.uqbar.heladeriakotlin.service.DuenioService
import org.uqbar.heladeriakotlin.service.HeladeriaService

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
        return heladeriaService.findByNombre(nombre).map { HeladeriaDTO.fromHeladeria(it) }
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
    fun actualizarHeladeria(
        @RequestBody heladeria: ActualizarHeladeriaDTO,
        @PathVariable heladeriaId: Long
    ): Heladeria {
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

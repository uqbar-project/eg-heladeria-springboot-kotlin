package org.uqbar.heladeriakotlin.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.uqbar.heladeriakotlin.dto.CredencialesDTO
import org.uqbar.heladeriakotlin.service.UsuarioService

@RestController
class UsuarioController {

    @Autowired
    lateinit var usuarioService: UsuarioService

    @PostMapping("/login")
    fun login(@RequestBody credencialesDTO: CredencialesDTO): String {
        return usuarioService.login(credencialesDTO)
    }
}
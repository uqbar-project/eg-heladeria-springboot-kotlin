package org.uqbar.heladeriakotlin.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.uqbar.heladeriakotlin.dto.CredencialesDTO
import org.uqbar.heladeriakotlin.service.UsuarioService

@RestController
@CrossOrigin(origins = ["**"])
class UsuarioController {

    @Autowired
    lateinit var usuarioService: UsuarioService

    @PostMapping("/login")
    fun login(@RequestBody credencialesDTO: CredencialesDTO): String {
        usuarioService.login(credencialesDTO)
        // return tokenUtils.createToken(credencialesDTO.usuario, credencialesDTO.password)!!
        return "ok"
    }
}
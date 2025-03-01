package org.uqbar.utils

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.gson.Gson
import org.uqbar.heladeriakotlin.dao.RepoUsuarios
import org.uqbar.heladeriakotlin.dto.CredencialesDTO
import org.uqbar.heladeriakotlin.model.Duenio
import org.uqbar.heladeriakotlin.model.Heladeria
import org.uqbar.heladeriakotlin.model.TipoHeladeria
import org.uqbar.heladeriakotlin.model.Usuario
import org.uqbar.heladeriakotlin.security.TokenUtils

fun Any.toJSON(): String {
    val gson = Gson()
    return gson.toJson(this)
}

object TestUtils {

    private val mapper = jacksonObjectMapper().apply {
        registerModule(JavaTimeModule())
    }

    fun crearUsuario(
        name: String,
        password: String,
        roles: String,
        repoUsuarios: RepoUsuarios,
        tokenUtils: TokenUtils
    ): String {
        val usuario = Usuario().apply {
            username = name
            crearPassword(password)
        }
        repoUsuarios.save(usuario)
        return "Bearer " + tokenUtils.createToken(usuario.username, listOf(roles))!!
    }


    fun getHeladeriaBase(): Heladeria {
        return Heladeria("Tucán", TipoHeladeria.ECONOMICA, Duenio("Carlos Martinelli", 1L), 1L).apply {
            this.gustos["Dulce de leche"] = 5
        }
    }

    fun bodyUsuarioExistente(): String = mapper.writeValueAsString(CredencialesDTO("elComun1", "password1"))

    fun bodyUsuarioPasswordIncorrecta(): String = mapper.writeValueAsString(CredencialesDTO("elComun1", "password2"))

    fun bodyUsuarioInexistente(): String =
        mapper.writeValueAsString(CredencialesDTO("usuarioInvalido", "cualquierPassword"))

    fun tokenUsuarioInvalido(): String =
        "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJsaXR1cmJlIiwiaWF0IjoxNjcwNTk0OTI5LCJleHAiOjE2NzE2NzQ5MjksInJvbGVzIjoiUk9MRV9VU0VSIn0.hSrd0sTw1OH57YlmV19xNCtide76AZa476XjPwE1uiW0wgbo7w5CarrJWCLjy0e62EZIbVjEGmIdHZ5tMHGkyg"
}
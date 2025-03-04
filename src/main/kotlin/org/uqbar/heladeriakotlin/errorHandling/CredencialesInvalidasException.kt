package org.uqbar.heladeriakotlin.errorHandling

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.UNAUTHORIZED)
class CredencialesInvalidasException(mensaje: String = "Las credenciales son inválidas") : RuntimeException(mensaje)

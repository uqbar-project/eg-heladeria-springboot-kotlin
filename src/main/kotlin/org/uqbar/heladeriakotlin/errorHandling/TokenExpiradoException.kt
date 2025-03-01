package org.uqbar.heladeriakotlin.errorHandling

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.FORBIDDEN)
class TokenExpiradoException(mensaje: String = "Token vencido") : RuntimeException(mensaje)

package org.uqbar.heladeriakotlin.errorHandling

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.UNAUTHORIZED)
class CredencialesInvalidasException : RuntimeException("Las credenciales son inválidas")

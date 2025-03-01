package org.uqbar.heladeriakotlin.errorHandling

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class BusinessException(message: String) : RuntimeException(message)

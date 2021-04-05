package br.com.exemploapi.infrastructure.exception

import org.springframework.http.HttpStatus

class SemResultadoException
(
  message: String = null,
  cause: Throwable = null
) extends BaseException(message, cause, HttpStatus.NO_CONTENT) {

}
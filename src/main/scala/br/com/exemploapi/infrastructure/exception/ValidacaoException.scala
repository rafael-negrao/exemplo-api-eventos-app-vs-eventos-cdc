package br.com.exemploapi.infrastructure.exception

import org.springframework.http.HttpStatus

class ValidacaoException
(
  message: String = null,
  cause: Throwable = null
) extends BaseException(message, cause, HttpStatus.UNPROCESSABLE_ENTITY) {

}
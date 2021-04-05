package br.com.exemploapi.infrastructure.exception

import org.springframework.http.HttpStatus

class AutorizacaoException
(
  message: String = null,
  cause: Throwable = null
) extends BaseException(message, cause, HttpStatus.UNAUTHORIZED) {

}
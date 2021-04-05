package br.com.exemploapi.infrastructure.exception

import org.springframework.http.HttpStatus

class InfraestruturaException
(
  message: String = null,
  cause: Throwable = null
) extends BaseException(message, cause, HttpStatus.INTERNAL_SERVER_ERROR) {

}
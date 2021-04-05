package br.com.exemploapi.infrastructure.exception

import br.com.exemploapi.infrastructure.dto.{DetalheErroDto, ResponseDto}
import org.springframework.http.{HttpStatus, ResponseEntity}

import java.util

class BaseException
(
  message: String = null,
  cause: Throwable = null,
  statusCode: HttpStatus = null,
  erros: util.List[DetalheErroDto] = new util.ArrayList[DetalheErroDto]()
) extends Exception(message, cause) with Serializable {

  def buildResponse(correlationId: String): ResponseEntity[ResponseDto] = {
    ResponseEntity
      .status(this.getStatusCode)
      .header("X-Correlation-ID", correlationId)
      .asInstanceOf[ResponseEntity.BodyBuilder]
      .body(ResponseDto(this.getCodigo, this.message, this.getErros))
  }

  def getErros: util.List[DetalheErroDto] = erros

  def getStatusCode: HttpStatus = statusCode

  def getCodigo: Int = this.getStatusCode.value

  def getMensagem: String = message
}

package br.com.exemploapi.infrastructure.exception

import br.com.exemploapi.infrastructure.dto.{DetalheErroDto, ResponseDto}
import org.springframework.beans.TypeMismatchException
import org.springframework.http.HttpStatus._
import org.springframework.http.{HttpHeaders, HttpStatus, ResponseEntity}
import org.springframework.web.bind.annotation.{ControllerAdvice, ExceptionHandler, ResponseStatus}
import org.springframework.web.bind.{MethodArgumentNotValidException, MissingServletRequestParameterException}
import org.springframework.web.context.request.WebRequest
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.multipart.support.MissingServletRequestPartException
import org.springframework.web.servlet.NoHandlerFoundException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import org.springframework.web.{HttpMediaTypeNotSupportedException, HttpRequestMethodNotSupportedException}

import scala.jdk.CollectionConverters._

@ControllerAdvice
case class RestExceptionHandler() extends ResponseEntityExceptionHandler {

  @ExceptionHandler(Array(classOf[ValidacaoException]))
  @ResponseStatus(UNPROCESSABLE_ENTITY)
  def validacaoException(ex: ValidacaoException, request: WebRequest): ResponseEntity[_] = ex.buildResponse("")

  @ExceptionHandler(value = Array(classOf[SemResultadoException]))
  @ResponseStatus(NO_CONTENT)
  def semResultadoException(ex: SemResultadoException): ResponseEntity[_] = ex.buildResponse("")

  @ExceptionHandler(value = Array(classOf[InfraestruturaException]))
  @ResponseStatus(UNPROCESSABLE_ENTITY)
  def infraestruturaException(e: InfraestruturaException): ResponseEntity[_] = e.buildResponse("")

  @ExceptionHandler(value = Array(classOf[BaseException]))
  @ResponseStatus(BAD_REQUEST)
  def baseException(e: InfraestruturaException): ResponseEntity[_] = e.buildResponse("")

  @ExceptionHandler(Array(classOf[Exception]))
  @ResponseStatus(INTERNAL_SERVER_ERROR)
  def handleAll(e: Exception, request: WebRequest): ResponseEntity[AnyRef] = {
    ResponseEntity.status(INTERNAL_SERVER_ERROR).body(ResponseDto(INTERNAL_SERVER_ERROR.value, e.getLocalizedMessage))
  }

  override protected def handleMethodArgumentNotValid(ex: MethodArgumentNotValidException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity[AnyRef] = {
    val responseDefault = ResponseDto(codigo = INTERNAL_SERVER_ERROR.value(), mensagem = ex.getLocalizedMessage)
    responseDefault.getErros.addAll(
      ex.getBindingResult
        .getFieldErrors()
        .asScala
        .map(error => DetalheErroDto(codigo = BAD_REQUEST.value, mensagem = s"${error.getField}: ${error.getDefaultMessage}"))
        .asJava
    )
    responseDefault.getErros.addAll(
      ex.getBindingResult
        .getGlobalErrors
        .asScala
        .map(error => DetalheErroDto(BAD_REQUEST.value, mensagem = s"${error.getObjectName}: ${error.getDefaultMessage}"))
        .asJava
    )
    ResponseEntity.status(responseDefault.codigo).body(responseDefault)
  }

  private def buildResponseDto(statusHttp: Int, e: Exception, error: String): ResponseEntity[AnyRef] = {
    val responseDefault = ResponseDto(codigo = statusHttp, mensagem = e.getLocalizedMessage)
    responseDefault.getErros.add(DetalheErroDto(mensagem = error))
    ResponseEntity.status(responseDefault.codigo).body(responseDefault)
  }

  override protected def handleTypeMismatch(e: TypeMismatchException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity[AnyRef] = {
    buildResponseDto(BAD_REQUEST.value, e, s"${e.getValue} valor para ${e.getPropertyName} deve ser do tipo ${e.getRequiredType}")
  }

  override protected def handleMissingServletRequestPart(e: MissingServletRequestPartException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity[AnyRef] = {
    buildResponseDto(BAD_REQUEST.value, e, s"${e.getRequestPartName} URL incompleta")
  }

  override protected def handleMissingServletRequestParameter(e: MissingServletRequestParameterException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity[AnyRef] = {
    buildResponseDto(BAD_REQUEST.value, e, s"${e.getParameterName} parametro não encontrado")
  }

  @ExceptionHandler(Array(classOf[MethodArgumentTypeMismatchException]))
  @ResponseStatus(BAD_REQUEST)
  def handleMethodArgumentTypeMismatch(e: MethodArgumentTypeMismatchException, request: WebRequest): ResponseEntity[AnyRef] = {
    buildResponseDto(BAD_REQUEST.value, e, s"${e.getName} deve ser do tipo ${e.getRequiredType.getName}")
  }

  override protected def handleNoHandlerFoundException(e: NoHandlerFoundException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity[AnyRef] = {
    buildResponseDto(UNPROCESSABLE_ENTITY.value, e, s"No handler found for ${e.getHttpMethod} ${e.getRequestURL}")
  }

  // 405
  override protected def handleHttpRequestMethodNotSupported(e: HttpRequestMethodNotSupportedException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity[AnyRef] = {
    buildResponseDto(METHOD_NOT_ALLOWED.value, e, s"'${e.getMethod}' Metodo não suportado para essa requisição. Metodos suportados são [${e.getSupportedHttpMethods.asScala.mkString(" ")}]")
  }

  // 415
  override protected def handleHttpMediaTypeNotSupported(e: HttpMediaTypeNotSupportedException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity[AnyRef] = {
    buildResponseDto(UNSUPPORTED_MEDIA_TYPE.value, e, s"'${e.getContentType}' ContentType não suportado para essa requisição. ContentType suportados são [${e.getSupportedMediaTypes.asScala.mkString(" ")}]")
  }

}

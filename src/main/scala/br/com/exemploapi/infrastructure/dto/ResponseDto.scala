package br.com.exemploapi.infrastructure.dto

import java.util

import com.fasterxml.jackson.annotation.JsonRootName

import scala.beans.BeanProperty

@JsonRootName("responseDefault")
case class ResponseDto
(
  @BeanProperty
  var codigo: Int = 0,

  @BeanProperty
  var mensagem: String = null,

  @BeanProperty
  var erros: util.List[DetalheErroDto] = new util.ArrayList[DetalheErroDto]()
) extends BaseDto {
}

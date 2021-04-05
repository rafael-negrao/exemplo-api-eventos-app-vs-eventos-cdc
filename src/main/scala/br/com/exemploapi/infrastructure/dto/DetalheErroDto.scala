package br.com.exemploapi.infrastructure.dto

import com.fasterxml.jackson.annotation.JsonRootName

import scala.beans.BeanProperty

@JsonRootName("detalheErro")
case class DetalheErroDto
(
  @BeanProperty
  var codigo: Int = 0,

  @BeanProperty
  var mensagem: String = null
) extends BaseDto {
}

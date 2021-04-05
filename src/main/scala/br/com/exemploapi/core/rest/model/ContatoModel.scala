package br.com.exemploapi.core.rest.model

import org.springframework.hateoas.RepresentationModel

import java.time.LocalDateTime
import scala.beans.BeanProperty

case class ContatoModel() extends RepresentationModel[ContatoModel] {

  @BeanProperty
  var id: Long = _

  @BeanProperty
  var nome: String = _

  @BeanProperty
  var telefone: String = _

  @BeanProperty
  var email: String = _

  @BeanProperty
  var dataCriacao: LocalDateTime = _

  @BeanProperty
  var dataAtualizacao: LocalDateTime = _

}

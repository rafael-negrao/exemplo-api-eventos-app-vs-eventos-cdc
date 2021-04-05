package br.com.exemploapi.core.entity

import java.time.LocalDateTime
import javax.persistence.{Entity, GeneratedValue, GenerationType, Id}
import scala.beans.BeanProperty

@Entity
case class ContatoEntity() {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
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

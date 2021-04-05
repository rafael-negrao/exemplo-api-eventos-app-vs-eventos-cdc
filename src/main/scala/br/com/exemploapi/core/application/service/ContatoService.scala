package br.com.exemploapi.core.application.service

import br.com.exemploapi.core.entity.ContatoEntity
import org.springframework.data.domain.{Page, Pageable}

trait ContatoService {

  def findAll(pageable: Pageable): Page[ContatoEntity]

  def findById(id: Long): Option[ContatoEntity]

  def create(contato: ContatoEntity): Option[ContatoEntity]

  def update(id: Long, contato: ContatoEntity): Option[ContatoEntity]

  def delete(id: Long): Option[ContatoEntity]
}

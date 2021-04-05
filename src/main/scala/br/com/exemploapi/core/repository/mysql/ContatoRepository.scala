package br.com.exemploapi.core.repository.mysql

import br.com.exemploapi.core.entity.ContatoEntity
import org.springframework.data.domain.{Page, Pageable}
import org.springframework.data.repository.CrudRepository

trait ContatoRepository extends CrudRepository[ContatoEntity, Long] {

  def findAllBy(pageable: Pageable): Page[ContatoEntity]

}

package br.com.exemploapi.core.application.service

import br.com.exemploapi.core.entity.ContatoEntity
import br.com.exemploapi.core.repository.mysql.ContatoRepository
import br.com.exemploapi.infrastructure.exception.SemResultadoException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.{Page, Pageable}
import org.springframework.stereotype.Service

import java.time.LocalDateTime

//import scala.jdk.OptionConverters._

@Service
case class ContatoServiceImpl() extends ContatoService {

  @Autowired
  var contatoRepository: ContatoRepository = _

  override def findAll(pageable: Pageable): Page[ContatoEntity] = {
    contatoRepository.findAllBy(pageable)
  }

  override def findById(id: Long): Option[ContatoEntity] = {
    val contatoOptional = contatoRepository.findById(id)
    if (contatoOptional.isPresent) Some(contatoOptional.get) else throw new SemResultadoException()
  }

  override def create(contato: ContatoEntity): Option[ContatoEntity] = {
    contato.dataCriacao = LocalDateTime.now()
    Some(contatoRepository.save(contato))
  }

  override def update(id: Long, contato: ContatoEntity): Option[ContatoEntity] = {
    findById(id)
      .map(contatoEntity => {
        contatoEntity.nome = contato.nome
        contatoEntity.email = contato.email
        contatoEntity.telefone = contato.telefone
        contatoEntity.dataAtualizacao = LocalDateTime.now()
        contatoRepository.save(contatoEntity)
      })
  }

  override def delete(id: Long): Option[ContatoEntity] = {
    val contatoEntity = findById(id)
    contatoRepository.deleteById(id)
    contatoEntity
  }
}

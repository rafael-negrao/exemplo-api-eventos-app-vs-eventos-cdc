package br.com.exemploapi.core.rest.web

import br.com.exemploapi.core.application.service.{ContatoService, EventAsyncService, Events}
import br.com.exemploapi.core.entity.ContatoEntity
import br.com.exemploapi.core.rest.assembers.ContatoModelAssembler
import br.com.exemploapi.core.rest.model.ContatoModel
import com.fasterxml.jackson.databind.ObjectMapper
import io.swagger.annotations.{Api, ApiOperation, ApiParam}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.{linkTo, methodOn}
import org.springframework.hateoas.{IanaLinkRelations, PagedModel}
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation._

import java.time.LocalDateTime
import scala.jdk.CollectionConverters._


@Api(value = "API responsável em fornecer as operações para a entidade Contato")
@RestController
@RequestMapping(Array("/api/contatos"))
class ContatoController {

  @Autowired
  var contatoService: ContatoService = _

  @Autowired
  var contatoModelAssembler: ContatoModelAssembler = _

  @Autowired
  var pagedResourcesAssembler: PagedResourcesAssembler[ContatoEntity] = _

  @Autowired
  var objectMapper: ObjectMapper = _

  @Autowired
  var eventAsyncService: EventAsyncService = _


  @ApiOperation("Metodo em responsavel em recuperar a lista de todos os contatos")
  @GetMapping(value = Array(""))
  def findAll
  (
    @ApiParam("Número da Página, a contagem começa do 0 (zero)")
    @RequestParam(value = "page_number", required = false, defaultValue = "0")
    pageNumber: Integer,

    @ApiParam("Número de registros por Página")
    @RequestParam(value = "page_size", required = false, defaultValue = "10")
    pageSize: Integer): ResponseEntity[PagedModel[ContatoModel]] = {

    val contatoEntityPage = contatoService.findAll(PageRequest.of(pageNumber, pageSize))

    val contatoModelPage: PagedModel[ContatoModel] = pagedResourcesAssembler.toModel(contatoEntityPage, contatoModelAssembler)

    ResponseEntity
      .ok()
      .body(contatoModelPage)
  }


  @ApiOperation("Metodo em responsavel em recuperar o contato usando o id como chave de pesquisa")
  @GetMapping(value = Array("/{id}"))
  def findById(@PathVariable("id") id: Long): ResponseEntity[ContatoModel] = {
    contatoService
      .findById(id)
      .map(contatoEntity =>
        ResponseEntity
          .ok()
          .body(contatoModelAssembler
            .toModel(contatoEntity)
            .add(linkTo(methodOn(classOf[ContatoController]).findAll(0, 10)).withRel("contatos"))
          )
      )
      .get
  }


  @ApiOperation("Metodo em responsavel em atualizar o contato usando o id como chave de pesquisa")
  @PutMapping(value = Array("/{id}"))
  def update
  (
    @PathVariable("id")
    id: Long,
    @RequestBody
    contatoEntity: ContatoEntity): ResponseEntity[ContatoModel] = {
    contatoService
      .update(id, contatoEntity)
      .map(contatoEntity => {
        val contatoModel = contatoModelAssembler.toModel(contatoEntity)
        eventAsyncService.producer(Events.CONTATOS, RequestMethod.PUT, LocalDateTime.now(), s"${Events.CONTATOS}_${contatoEntity.id}", contatoModel)
        contatoModel
      })
      .map(contatoModel =>
        ResponseEntity
          .created(contatoModel.getRequiredLink(IanaLinkRelations.SELF).toUri)
          .body(contatoModel))
      .get
  }


  @ApiOperation("Metodo em responsavel em criar um novo contato")
  @PostMapping(value = Array(""))
  def create(@RequestBody contatoEntity: ContatoEntity): ResponseEntity[ContatoModel] = {
    contatoService
      .create(contatoEntity)
      .map(contatoEntity => {
        val contatoModel = contatoModelAssembler.toModel(contatoEntity)
        eventAsyncService.producer(Events.CONTATOS, RequestMethod.POST, LocalDateTime.now(), s"${Events.CONTATOS}_${contatoModel.id}", contatoModel)
        contatoModel
      })
      .map(contatoModel =>
        ResponseEntity
          .created(contatoModel.getRequiredLink(IanaLinkRelations.SELF).toUri)
          .body(contatoModel))
      .get
  }


  @ApiOperation("Metodo em responsavel em remover o contato")
  @DeleteMapping(value = Array("/{id}"))
  def delete(@PathVariable("id") id: Long): ResponseEntity[Void] = {
    contatoService.delete(id)
    eventAsyncService.producer(Events.CONTATOS, RequestMethod.DELETE, LocalDateTime.now(), s"${Events.CONTATOS}_$id", Map("id" -> id).asJava)
    ResponseEntity.noContent().build()
  }
}

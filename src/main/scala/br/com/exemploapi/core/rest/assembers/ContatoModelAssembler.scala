package br.com.exemploapi.core.rest.assembers

import br.com.exemploapi.core.entity.ContatoEntity
import br.com.exemploapi.core.rest.model.ContatoModel
import br.com.exemploapi.core.rest.web.ContatoController
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.{linkTo, methodOn}
import org.springframework.stereotype.Component

@Component
class ContatoModelAssembler extends RepresentationModelAssemblerSupport[ContatoEntity, ContatoModel](classOf[ContatoController], classOf[ContatoModel]) {

  @Autowired
  var objectMapper: ObjectMapper = _

  override def toModel(entity: ContatoEntity): ContatoModel = {
    val model = objectMapper.convertValue(entity, classOf[ContatoModel])
    model.add(linkTo(methodOn(classOf[ContatoController]).findById(model.id)).withSelfRel())
    model
  }

  override def toCollectionModel(entities: java.lang.Iterable[_ <: ContatoEntity]): CollectionModel[ContatoModel] = {
    val models = super.toCollectionModel(entities)
    models.add(linkTo(methodOn(classOf[ContatoController]).findAll(0, 10)).withSelfRel())
    models
  }
}

package br.com.exemploapi.core.application.service

import br.com.exemploapi.core.entity.ContatoEntity
import br.com.exemploapi.core.repository.kafka.KafkaRepository
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.{ObjectMapper, SerializationFeature}
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.support.SendResult
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.util.concurrent.ListenableFuture
import org.springframework.web.bind.annotation.RequestMethod

import java.time.LocalDateTime
import scala.jdk.CollectionConverters._

@Service
case class EventAsyncServiceImpl() extends EventAsyncService {

  @Autowired
  var kafkaRepository: KafkaRepository = _

  @Autowired
  var objectMapper: ObjectMapper = _

  @Async
  def producer(eventName: String, requestMethod: RequestMethod, dateTime: LocalDateTime, key: String, message: Any): ListenableFuture[SendResult[String, String]] = {
    kafkaRepository.sendMessage(
      eventName,
      key,
      objectMapper.writeValueAsString(
        Map(
          "requestMethod" -> requestMethod,
          "dateTime" -> dateTime,
          "message" -> message
        ).asJava
      )
    )
  }
}

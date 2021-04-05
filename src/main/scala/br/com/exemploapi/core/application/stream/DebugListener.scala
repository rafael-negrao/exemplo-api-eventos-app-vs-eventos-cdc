package br.com.exemploapi.core.application.stream

import org.apache.kafka.clients.consumer.{ConsumerRecord, KafkaConsumer}
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.{Acknowledgment, KafkaHeaders}
import org.springframework.messaging.handler.annotation.{Header, Payload}
import org.springframework.stereotype.Component

@Component
case class DebugListener() {

  private val LOGGER = LoggerFactory.getLogger(classOf[DebugListener])

  @KafkaListener(topics = Array("contatos", "mysql.exemplodb.contato_entity"), groupId = "debug001")
  def debug
  (
    @Header(KafkaHeaders.RECEIVED_TOPIC) topic: String,
    @Header(KafkaHeaders.RECEIVED_PARTITION_ID) partition: Int,
    @Payload message: String
  ): Unit = {
    LOGGER.debug(s"topico: [$topic], partition: [$partition], message: [$message]")
//    acknowledgment.acknowledge() // commit
  }

}

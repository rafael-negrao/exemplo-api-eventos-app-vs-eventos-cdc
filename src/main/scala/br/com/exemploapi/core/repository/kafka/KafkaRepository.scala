package br.com.exemploapi.core.repository.kafka

import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Repository
import org.springframework.util.concurrent.{ListenableFuture, ListenableFutureCallback}


@Repository
case class KafkaRepository() {

  private val LOGGER = LoggerFactory.getLogger(classOf[KafkaRepository])

  @Autowired
  val kafkaTemplate: KafkaTemplate[String, String] = null

  def sendMessage(topicName: String, key: String, message: String): ListenableFuture[SendResult[String, String]] = {
    val future = kafkaTemplate.send(topicName, key, message)
    future.addCallback(new ListenableFutureCallback[SendResult[String, String]]() {
      override def onSuccess(result: SendResult[String, String]): Unit = {
        LOGGER.debug(s"Sent message: [$message] with offset=[${result.getRecordMetadata.offset()}]")
      }
      override def onFailure(ex: Throwable): Unit = {
        LOGGER.error(s"Unable to send message=[$message] ] due to : [${ex.getMessage}]")
      }
    })
    future
  }

}

package br.com.exemploapi.infrastructure.config

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.core.{DefaultKafkaProducerFactory, KafkaTemplate, ProducerFactory}

import scala.jdk.CollectionConverters._

@Configuration
@EnableKafka
case class KafkaConfig() {

  @Value(value = "${spring.kafka.bootstrap-servers}")
  var bootstrapServersKafka: String = _

  @Bean
  def producerFactory: ProducerFactory[String, String] = {
    val configProps = Map[String, Object](
      ProducerConfig.BOOTSTRAP_SERVERS_CONFIG -> bootstrapServersKafka,
      ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG -> classOf[StringSerializer],
      ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG -> classOf[StringSerializer],
      ProducerConfig.ACKS_CONFIG -> "all"
    )
    new DefaultKafkaProducerFactory[String, String](configProps.asJava)
  }

  @Bean
  def kafkaTemplate = new KafkaTemplate[String, String](producerFactory)
}

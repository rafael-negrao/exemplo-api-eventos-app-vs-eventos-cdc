package br.com.exemploapi

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.{ObjectMapper, SerializationFeature}
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.filter.ForwardedHeaderFilter


@SpringBootApplication
case class Application() {

  @Bean
  def objectMapper: ObjectMapper = {
    val objectMapper = new ObjectMapper
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS)
    objectMapper
  }

  @Bean def forwardedHeaderFilter = new ForwardedHeaderFilter()

}

case object Application extends App {

  val application: SpringApplication = new SpringApplication(classOf[Application])

  val configurableApplicationContext = application.run(args:_*)

}

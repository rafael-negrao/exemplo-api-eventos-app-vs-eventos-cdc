package br.com.exemploapi.core.application.service

import org.springframework.kafka.support.SendResult
import org.springframework.util.concurrent.ListenableFuture
import org.springframework.web.bind.annotation.RequestMethod

import java.time.LocalDateTime

trait EventAsyncService {

  def producer(eventName: String, requestMethod: RequestMethod, dateTime: LocalDateTime, key: String, message: Any): ListenableFuture[SendResult[String, String]]

}

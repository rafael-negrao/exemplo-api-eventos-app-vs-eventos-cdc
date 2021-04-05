package br.com.exemploapi.infrastructure.config

import org.slf4j.LoggerFactory
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.scheduling.annotation.{AsyncConfigurerSupport, EnableAsync, EnableScheduling}
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

import java.lang.reflect.Method
import java.util.concurrent.Executor
import javax.annotation.Resource


@Configuration
@EnableAsync
@EnableScheduling
case class AsyncConfig() extends AsyncConfigurerSupport {

  private val LOGGER = LoggerFactory.getLogger(classOf[AsyncConfig])

  val pool = new ThreadPoolTaskExecutor

  @Bean
  def taskExecutor: ThreadPoolTaskExecutor = {
    pool.setCorePoolSize(10)
    pool.setMaxPoolSize(10)
    pool.setWaitForTasksToCompleteOnShutdown(true)
    pool
  }

  override def getAsyncExecutor: Executor = this.taskExecutor

  override def getAsyncUncaughtExceptionHandler: AsyncUncaughtExceptionHandler = {
    new AsyncUncaughtExceptionHandler() {
      override def handleUncaughtException(ex: Throwable, method: Method, params: AnyRef*): Unit = {
        taskExecutor.shutdown()
        LOGGER.error(s"Erro ao processar a daemon", ex)
        System.exit(1)
      }
    }
  }

}

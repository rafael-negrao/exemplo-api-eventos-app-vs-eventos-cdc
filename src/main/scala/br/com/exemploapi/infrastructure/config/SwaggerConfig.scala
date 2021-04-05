package br.com.exemploapi.infrastructure.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.{Bean, Configuration}
import springfox.documentation.builders.{ApiInfoBuilder, RequestHandlerSelectors}
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
case class SwaggerConfig() {

  @Value("${app.version}")
  private var versao: String = null

  @Value("${spring.application.name}")
  private var applicationName: String = null

  @Bean
  def productApi =
    new Docket(DocumentationType.SWAGGER_2)
      .select
      .apis(RequestHandlerSelectors.basePackage("br.com.exemploapi.core.rest.web"))
      .build
      .apiInfo(apiInfo)

  private def apiInfo =
    new ApiInfoBuilder()
      .title(s"Documentação da API ${applicationName}")
      .version(versao)
      .build
}
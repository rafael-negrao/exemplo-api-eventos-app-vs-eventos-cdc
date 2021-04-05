package br.com.exemploapi.infrastructure.config

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Bean
import org.springframework.hateoas.client.LinkDiscoverer
import org.springframework.hateoas.client.LinkDiscoverers
import org.springframework.hateoas.mediatype.collectionjson.CollectionJsonLinkDiscoverer
import org.springframework.plugin.core.SimplePluginRegistry
import java.util

@Configuration
case class HateoasConfig() {

  @Bean
  def discoverers: LinkDiscoverers = {
    val plugins = new util.ArrayList[LinkDiscoverer]
    plugins.add(new CollectionJsonLinkDiscoverer)
    new LinkDiscoverers(SimplePluginRegistry.create(plugins))
  }

}

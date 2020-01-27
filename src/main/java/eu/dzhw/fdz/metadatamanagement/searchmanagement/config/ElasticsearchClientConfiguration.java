package eu.dzhw.fdz.metadatamanagement.searchmanagement.config;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.elasticsearch.rest.RestClientAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.rest.RestClientBuilderCustomizer;
import org.springframework.boot.autoconfigure.elasticsearch.rest.RestClientProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.RelatedQuestionSubDocumentProjectionAdapter;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.projections.RelatedQuestionSubDocumentProjection;

/**
 * Spring Configuration which instantiates the Jest Elasticsearch Client.
 * 
 * @author René Reitmann
 */
@Configuration
public class ElasticsearchClientConfiguration {
  /**
   * Configure elasticsearch json serialization / deserialization.
   * 
   * @return the configured gson mapper
   */
  @Bean
  public Gson gson() {
    Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateConverter())
        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeConverter())
        .registerTypeAdapter(RelatedQuestionSubDocumentProjection.class,
            new RelatedQuestionSubDocumentProjectionAdapter())
        .create();
    return gson;
  }

  /**
   * This is only necesssary cause spring boot does not respect credential in the uri.
   * @param properties spring boot properties
   * @param builderCustomizers client customizer
   * @see RestClientAutoConfiguration
   */
  @Bean
  public RestClientBuilder elasticsearchRestClientBuilder(RestClientProperties properties,
      ObjectProvider<RestClientBuilderCustomizer> builderCustomizers) {
    UriComponents uriComponents =
        UriComponentsBuilder.fromUriString(properties.getUris().get(0)).build();
    HttpHost[] hosts;
    if (StringUtils.hasLength(uriComponents.getUserInfo())) {
      String[] usernamePassword = uriComponents.getUserInfo().split(":");
      properties.setUsername(usernamePassword[0]);
      properties.setPassword(usernamePassword[1]);
      hosts = properties.getUris().stream()
          .map(uri -> uri.replaceAll(uriComponents.getUserInfo() + "@", "")).map(HttpHost::create)
          .toArray(HttpHost[]::new);
    } else {      
      hosts = properties.getUris().stream().map(HttpHost::create).toArray(HttpHost[]::new);
    }
    RestClientBuilder builder = RestClient.builder(hosts);
    PropertyMapper map = PropertyMapper.get();
    map.from(properties::getUsername).whenHasText().to((username) -> {
      CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
      Credentials credentials =
          new UsernamePasswordCredentials(properties.getUsername(), properties.getPassword());
      credentialsProvider.setCredentials(AuthScope.ANY, credentials);
      builder.setHttpClientConfigCallback((httpClientBuilder) -> httpClientBuilder
          .setDefaultCredentialsProvider(credentialsProvider));
    });
    builder.setRequestConfigCallback((requestConfigBuilder) -> {
      map.from(properties::getConnectionTimeout).whenNonNull().asInt(Duration::toMillis)
          .to(requestConfigBuilder::setConnectTimeout);
      map.from(properties::getReadTimeout).whenNonNull().asInt(Duration::toMillis)
          .to(requestConfigBuilder::setSocketTimeout);
      return requestConfigBuilder;
    });
    builderCustomizers.orderedStream().forEach((customizer) -> customizer.customize(builder));
    return builder;
  }
}

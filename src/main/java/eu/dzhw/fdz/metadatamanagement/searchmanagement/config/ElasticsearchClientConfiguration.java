package eu.dzhw.fdz.metadatamanagement.searchmanagement.config;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}

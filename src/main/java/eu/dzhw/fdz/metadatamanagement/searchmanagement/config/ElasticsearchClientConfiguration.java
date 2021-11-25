package eu.dzhw.fdz.metadatamanagement.searchmanagement.config;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.AbstractAnalysisDataPackage;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.CustomDataPackage;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.DataPackage;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.ExternalDataPackage;
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
    RuntimeTypeAdapterFactory<AbstractAnalysisDataPackage> adapterFactory =
        RuntimeTypeAdapterFactory.of(AbstractAnalysisDataPackage.class, "type")
            .registerSubtype(ExternalDataPackage.class, "externalData")
            .registerSubtype(DataPackage.class, "dataPackage")
            .registerSubtype(CustomDataPackage.class, "customDataPackage");
    Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateConverter())
        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeConverter())
        .registerTypeAdapter(RelatedQuestionSubDocumentProjection.class,
            new RelatedQuestionSubDocumentProjectionAdapter())
        .registerTypeAdapterFactory(adapterFactory).create();
    return gson;
  }
}

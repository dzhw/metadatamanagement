package eu.dzhw.fdz.metadatamanagement.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.SerializationFeature;

import eu.dzhw.fdz.metadatamanagement.common.components.StringTrimModule;

/**
 * Configure the jackson json mapper.
 */
@Configuration
public class JacksonConfiguration {

  @Bean
  Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
    // enable custom data bindings like JSR 310 (e.g. LocalDate)
    return new Jackson2ObjectMapperBuilder()
      .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
      .serializationInclusion(Include.NON_NULL)
      .modulesToInstall(new StringTrimModule());
  }
}

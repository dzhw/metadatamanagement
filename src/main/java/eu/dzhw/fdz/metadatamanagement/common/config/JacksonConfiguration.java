package eu.dzhw.fdz.metadatamanagement.common.config;

import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonInclude;

import eu.dzhw.fdz.metadatamanagement.common.config.jackson.StringTrimModule;
import tools.jackson.databind.cfg.DateTimeFeature;

/**
 * Configure the (Jackson 3) json mapper used by Spring MVC and Spring Data Rest.
 */
@Configuration
public class JacksonConfiguration {

  @Bean
  JsonMapperBuilderCustomizer mdmJsonMapperBuilderCustomizer() {
    // JSR 310 (e.g. LocalDate) support is built into Jackson 3
    return builder -> builder
      .disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS)
      .changeDefaultPropertyInclusion(
          inclusion -> inclusion.withValueInclusion(JsonInclude.Include.NON_NULL))
      .addModule(new StringTrimModule());
  }
}

package eu.dzhw.fdz.metadatamanagement.config;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import eu.dzhw.fdz.metadatamanagement.domain.util.Jsr310DateTimeSerializer;
import eu.dzhw.fdz.metadatamanagement.domain.util.Jsr310LocalDateDeserializer;

/**
 * Configure the jackson json mapper.
 */
@Configuration
public class JacksonConfiguration {

  @Bean
  Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
    JavaTimeModule module = new JavaTimeModule();
    module.addSerializer(OffsetDateTime.class,
        Jsr310DateTimeSerializer.getJsr310DateTimeSerializer());
    module.addSerializer(ZonedDateTime.class,
        Jsr310DateTimeSerializer.getJsr310DateTimeSerializer());
    module.addSerializer(LocalDateTime.class,
        Jsr310DateTimeSerializer.getJsr310DateTimeSerializer());
    module.addSerializer(Instant.class, Jsr310DateTimeSerializer.getJsr310DateTimeSerializer());
    module.addDeserializer(LocalDate.class,
        Jsr310LocalDateDeserializer.getJsr310LocalDateDeserializer());
    return new Jackson2ObjectMapperBuilder()
      .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
      .findModulesViaServiceLoader(true)
      .modulesToInstall(module);
  }
}

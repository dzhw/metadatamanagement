package eu.dzhw.fdz.metadatamanagement.config.elasticsearch;

import java.io.IOException;

import org.springframework.data.elasticsearch.core.EntityMapper;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * A custom json mapper which knows how to map JSR 310 types (e.g. LocalDate)
 * 
 * @author René Reitmann
 */
public class JacksonDocumentMapper implements EntityMapper {

  private ObjectMapper objectMapper;

  /**
   * Initialize the jackson object mapper.
   */
  public JacksonDocumentMapper() {
    objectMapper = new ObjectMapper();
    // enable custom data bindings like JSR 310 (e.g. LocalDate)
    objectMapper.findAndRegisterModules();
    // use iso encoding for dates
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    // copied from ElasticsearchTemplate
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
  }

  @Override
  public String mapToString(Object object) throws IOException {
    return objectMapper.writeValueAsString(object);
  }

  @Override
  public <T> T mapToObject(String source, Class<T> clazz) throws IOException {
    return objectMapper.readValue(source, clazz);
  }

}

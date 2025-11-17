package eu.dzhw.fdz.metadatamanagement.searchmanagement.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.RelatedQuestionSubDocument;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.projections.RelatedQuestionSubDocumentProjection;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;

/**
 * Spring Configuration which instantiates the Elasticsearch REST Client.
 */
@Configuration
@Slf4j
public class ElasticsearchClientConfiguration {

  private final ObjectMapper objectMapper;

  public ElasticsearchClientConfiguration() {

    // configure marshaling for custom data types
    var customMdmModule = new SimpleModule();
    customMdmModule.addSerializer(LocalDate.class, new LocalDateSerializer());
    customMdmModule.addDeserializer(LocalDate.class, new LocalDateDeserializer());
    customMdmModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
    customMdmModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
    customMdmModule.addSerializer(RelatedQuestionSubDocumentProjection.class, new RelatedQuestionSubDocumentProjectionSerializer());
    customMdmModule.addDeserializer(RelatedQuestionSubDocumentProjection.class, new RelatedQuestionSubDocumentProjectionDeserializer());

    this.objectMapper = new ObjectMapper()
      .registerModule(customMdmModule);
  }

  @Value("${spring.elasticsearch.rest.uris[0]}")
  private String url;

  @Bean(destroyMethod = "close")
  public ElasticsearchClient elasticsearchClient() {

    // extract basic auth credentials from URL if necessary and prepare auth header
    final var headers = new ArrayList<Header>();
    final var chunks = this.url.split("@");
    var host = chunks.length == 2 ? chunks[1] : url;
    if (chunks.length == 2) {
      host = this.url.startsWith("https") ? "https://" + host : "http://" + host;
      final var credentials = chunks[0]
        .replace("http://", "")
        .replace("https://", "");
      final var encoded = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
      headers.add(new BasicHeader("Authorization", "Basic " + encoded));
    }

    final var restClient = RestClient.builder(HttpHost.create(host))
      .setDefaultHeaders(headers.toArray(new Header[]{}))
      .build();
    final var jsonMapper = new JacksonJsonpMapper(this.objectMapper);
    final var transport = new RestClientTransport(restClient, jsonMapper);
    return new ElasticsearchClient(transport);
  }

  private static class LocalDateSerializer extends JsonSerializer<LocalDate> {
    @Override
    public void serialize(LocalDate localDate, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
      jsonGenerator.writeString(localDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
    }
  }

  private static class LocalDateDeserializer extends JsonDeserializer<LocalDate> {
    @Override
    public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
      return LocalDate.parse(jsonParser.getText(), DateTimeFormatter.ISO_LOCAL_DATE);
    }
  }

  private static class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
    @Override
    public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
      jsonGenerator.writeString(localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }
  }

  private static class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
      return LocalDateTime.parse(jsonParser.getText(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
  }

  private static class RelatedQuestionSubDocumentProjectionSerializer extends JsonSerializer<RelatedQuestionSubDocumentProjection> {
    @Override
    public void serialize(RelatedQuestionSubDocumentProjection value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
      jsonGenerator.writeStartObject();
      jsonGenerator.writeStringField("instrumentId", value.getInstrumentId());
      jsonGenerator.writeStringField("questionId", value.getQuestionId());
      jsonGenerator.writeEndObject();
    }
  }

  private static class RelatedQuestionSubDocumentProjectionDeserializer extends JsonDeserializer<RelatedQuestionSubDocumentProjection> {
    @Override
    public RelatedQuestionSubDocumentProjection deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
      var relatedQuestion = new RelatedQuestionSubDocument();
      while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
        switch (jsonParser.getCurrentName()) {
          case "instrumentId":
            relatedQuestion.setInstrumentId(jsonParser.getValueAsString());
            break;
          case "questionId":
            relatedQuestion.setQuestionId(jsonParser.getValueAsString());
            break;
          default:
            break;
        }
      }
      return relatedQuestion;
    }
  }
}

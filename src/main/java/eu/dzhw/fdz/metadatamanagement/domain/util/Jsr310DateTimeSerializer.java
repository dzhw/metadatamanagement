package eu.dzhw.fdz.metadatamanagement.domain.util;

import java.io.IOException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Serialize {@link TemporalAccessor} to JSON.
 */
public final class Jsr310DateTimeSerializer extends JsonSerializer<TemporalAccessor> {

  private static final DateTimeFormatter ISOFormatter =
      DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));

  public static volatile Jsr310DateTimeSerializer DATE_TIME_SERIALIZER;

  /**
   * Private Constructor for singleton.
   */
  private Jsr310DateTimeSerializer() {}

  @Override
  public void serialize(TemporalAccessor value, JsonGenerator generator,
      SerializerProvider serializerProvider) throws IOException {
    generator.writeString(ISOFormatter.format(value));
  }

  /**
   * Return the singleton instance of the jsr 310 datetime serializer.
   */
  public static Jsr310DateTimeSerializer getJsr310DateTimeSerializer() {

    if (DATE_TIME_SERIALIZER == null) {
      synchronized (Jsr310DateTimeSerializer.class) {
        if (DATE_TIME_SERIALIZER == null) {
          DATE_TIME_SERIALIZER = new Jsr310DateTimeSerializer();
        }
      }
    }

    return DATE_TIME_SERIALIZER;
  }
}

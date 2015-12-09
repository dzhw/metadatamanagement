package eu.dzhw.fdz.metadatamanagement.domain.util;

import java.io.IOException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * @author Daniel Katzberg
 * @author JHipster
 */
public final class JSR310DateTimeSerializer extends JsonSerializer<TemporalAccessor> {

  private static final DateTimeFormatter ISOFormatter =
      DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));

  public static volatile JSR310DateTimeSerializer DATE_TIME_SERIALIZER;

  /**
   * Private Constructor for singleton.
   */
  private JSR310DateTimeSerializer() {}

  @Override
  public void serialize(TemporalAccessor value, JsonGenerator generator,
      SerializerProvider serializerProvider) throws IOException {
    generator.writeString(ISOFormatter.format(value));
  }

  /**
   * @return the singleton instance of the jsr 310 datetime serializer
   */
  public static JSR310DateTimeSerializer getJSR310DateTimeSerializer() {

    if (DATE_TIME_SERIALIZER == null) {
      synchronized (JSR310DateTimeSerializer.class) {
        if (DATE_TIME_SERIALIZER == null) {
          DATE_TIME_SERIALIZER = new JSR310DateTimeSerializer();
        }
      }
    }

    return DATE_TIME_SERIALIZER;
  }
}

package eu.dzhw.fdz.metadatamanagement.domain.util;

import java.io.IOException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * A JSR 310 serializer for date and time.
 */
public final class Jsr310DateTimeSerializer extends JsonSerializer<TemporalAccessor> {

  private static final DateTimeFormatter ISOFormatter =
      DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));

  public static final Jsr310DateTimeSerializer INSTANCE = new Jsr310DateTimeSerializer();

  private Jsr310DateTimeSerializer() {}

  @Override
  public void serialize(TemporalAccessor value, JsonGenerator generator,
      SerializerProvider serializerProvider) throws IOException {
    generator.writeString(ISOFormatter.format(value));
  }
}

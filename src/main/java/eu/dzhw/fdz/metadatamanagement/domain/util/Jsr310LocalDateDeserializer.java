package eu.dzhw.fdz.metadatamanagement.domain.util;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * Custom Jackson deserializer for transforming a JSON object (using the ISO 8601 date formatwith
 * optional time) to a JSR310 LocalDate object.
 * 
 * @author Daniel Katzberg
 * @author JHipster
 */
public class Jsr310LocalDateDeserializer extends JsonDeserializer<LocalDate> {

  private static volatile Jsr310LocalDateDeserializer LOCAL_DATE_DESERIALIZER;
  
  private static final DateTimeFormatter ISO_DATE_OPTIONAL_TIME;

  /** Singleton private Constructor. */
  private Jsr310LocalDateDeserializer() {}  

  static {
    ISO_DATE_OPTIONAL_TIME = new DateTimeFormatterBuilder().append(DateTimeFormatter.ISO_LOCAL_DATE)
      .optionalStart()
      .appendLiteral('T')
      .append(DateTimeFormatter.ISO_OFFSET_TIME)
      .toFormatter();
  }

  @Override
  public LocalDate deserialize(JsonParser parser, DeserializationContext context)
      throws IOException {
    switch (parser.getCurrentToken()) {
      case START_ARRAY:
        if (parser.nextToken() == JsonToken.END_ARRAY) {
          return null;
        }
        int year;
        year = parser.getIntValue();

        parser.nextToken();
        int month = parser.getIntValue();

        parser.nextToken();
        int day = parser.getIntValue();

        if (parser.nextToken() != JsonToken.END_ARRAY) {
          throw context.wrongTokenException(parser, JsonToken.END_ARRAY, "Expected array to end.");
        }
        return LocalDate.of(year, month, day);

      case VALUE_STRING:
        String string = parser.getText()
            .trim();
        if (string.length() == 0) {
          return null;
        }
        return LocalDate.parse(string, ISO_DATE_OPTIONAL_TIME);
      default:
        break;
    }
    throw context.wrongTokenException(parser, JsonToken.START_ARRAY, "Expected array or string.");
  }

  /**
   * Get the {@link Jsr310LocalDateDeserializer}.
   */
  public static Jsr310LocalDateDeserializer getJsr310LocalDateDeserializer() {

    if (LOCAL_DATE_DESERIALIZER == null) {
      synchronized (Jsr310LocalDateDeserializer.class) {
        if (LOCAL_DATE_DESERIALIZER == null) {
          LOCAL_DATE_DESERIALIZER = new Jsr310LocalDateDeserializer();
        }
      }
    }

    return LOCAL_DATE_DESERIALIZER;
  }
}

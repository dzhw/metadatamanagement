package eu.dzhw.fdz.metadatamanagement.searchmanagement.config;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * Serialize/Deserialize {@link LocalDateTime}s going to / coming from elasticsearch.
 * 
 * @author René Reitmann
 */
public class LocalDateTimeConverter implements JsonSerializer<LocalDateTime>, 
    JsonDeserializer<LocalDateTime> {

  @Override
  public LocalDateTime deserialize(JsonElement json, Type type, 
      JsonDeserializationContext context)
      throws JsonParseException {
    return LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
  }
  
  @Override
  public JsonElement serialize(LocalDateTime localDate,
      Type type, JsonSerializationContext context) {
    return new JsonPrimitive(localDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
  }
}

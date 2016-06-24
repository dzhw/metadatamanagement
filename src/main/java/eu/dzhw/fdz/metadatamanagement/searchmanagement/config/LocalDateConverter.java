package eu.dzhw.fdz.metadatamanagement.searchmanagement.config;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * Serialize/Deserialize {@link LocalDate}s going to / coming from elasticsearch.
 * 
 * @author René Reitmann
 */
public class LocalDateConverter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {

  @Override
  public LocalDate deserialize(JsonElement json, Type type, JsonDeserializationContext context)
      throws JsonParseException {
    return LocalDate.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE);
  }
  
  @Override
  public JsonElement serialize(LocalDate localDate, Type type, JsonSerializationContext context) {
    return new JsonPrimitive(localDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
  }
}

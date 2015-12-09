/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.domain.util;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.LocalDate;

import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * @author Daniel Katzberg
 *
 */
public class JSR310LocalDateDeserializerTest {

  @Test
  public void testDeserializeAccetable() throws IOException {

    // Arrange
    JSR310LocalDateDeserializer deserializer =
        JSR310LocalDateDeserializer.getJSR310LocalDateDeserializer();
    JsonParser jsonParser = Mockito.mock(JsonParser.class);
    when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);
    when(jsonParser.nextToken()).thenReturn(null, JsonToken.END_ARRAY);
    when(jsonParser.getIntValue()).thenReturn(2015, 10, 31);
    DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);

    // Act
    LocalDate localDate = deserializer.deserialize(jsonParser, deserializationContext);

    // Assert
    assertThat(localDate.toString(), is("2015-10-31"));
  }

  @Test
  public void testDeserializeReturnNull() throws IOException {

    // Arrange
    JSR310LocalDateDeserializer deserializer =
        JSR310LocalDateDeserializer.getJSR310LocalDateDeserializer();
    JsonParser jsonParser = Mockito.mock(JsonParser.class);
    when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);
    when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);
    DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);

    // Act
    LocalDate localDate = deserializer.deserialize(jsonParser, deserializationContext);

    // Assert
    assertThat(localDate, is(nullValue()));
  }

  @Test(expected = JsonMappingException.class)
  public void testDeserializeWithException() throws IOException {

    // Arrange
    JSR310LocalDateDeserializer deserializer =
        JSR310LocalDateDeserializer.getJSR310LocalDateDeserializer();
    JsonParser jsonParser = Mockito.mock(JsonParser.class);
    when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);
    when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
    DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
    when(deserializationContext.wrongTokenException(anyObject(), anyObject(), anyString()))
      .thenReturn(new JsonMappingException("Expected array to end."));

    // Act
    deserializer.deserialize(jsonParser, deserializationContext);

    // Asserts
  }

  @Test
  public void testDeserializeAccetableValueString() throws IOException {

    // Arrange
    JSR310LocalDateDeserializer deserializer =
        JSR310LocalDateDeserializer.getJSR310LocalDateDeserializer();
    JsonParser jsonParser = Mockito.mock(JsonParser.class);
    when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
    when(jsonParser.getText()).thenReturn("2015-10-31");
    DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);

    // Act
    LocalDate localDate = deserializer.deserialize(jsonParser, deserializationContext);

    // Assert
    assertThat(localDate.toString(), is("2015-10-31"));
  }
  
  @Test
  public void testDeserializeAccetableValueEmptyString() throws IOException {

    // Arrange
    JSR310LocalDateDeserializer deserializer =
        JSR310LocalDateDeserializer.getJSR310LocalDateDeserializer();
    JsonParser jsonParser = Mockito.mock(JsonParser.class);
    when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
    when(jsonParser.getText()).thenReturn("");
    DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);

    // Act
    LocalDate localDate = deserializer.deserialize(jsonParser, deserializationContext);

    // Assert
    assertThat(localDate, is(nullValue()));
  }

  @Test(expected = JsonMappingException.class)
  public void testDeserializeNoAccetable() throws IOException {

    // Arrange
    JSR310LocalDateDeserializer deserializer =
        JSR310LocalDateDeserializer.getJSR310LocalDateDeserializer();
    JsonParser jsonParser = Mockito.mock(JsonParser.class);
    when(jsonParser.getCurrentToken()).thenReturn(JsonToken.END_ARRAY);
    DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
    when(deserializationContext.wrongTokenException(anyObject(), anyObject(), anyString()))
      .thenReturn(new JsonMappingException("Expected array to end."));

    // Act
    deserializer.deserialize(jsonParser, deserializationContext);

    // Assert
  }
}

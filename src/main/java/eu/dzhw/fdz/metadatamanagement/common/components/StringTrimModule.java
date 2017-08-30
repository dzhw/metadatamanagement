package eu.dzhw.fdz.metadatamanagement.common.components;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Trims all String of imported data. 
 * Reference (Code from): https://stackoverflow.com/a/33765854/8080153
 * 
 * @author Daniel Katzberg
 */
@Component
public class StringTrimModule extends SimpleModule {
 
  private static final long serialVersionUID = 1L;
  
  /**
   * Trims all String on an import.
   */
  @SuppressFBWarnings({"SIC_INNER_SHOULD_BE_STATIC_ANON", "SE_INNER_CLASS"})  
  public StringTrimModule() {
    addDeserializer(String.class, new StdScalarDeserializer<String>(String.class) {

      /*
       * (non-Javadoc)
       * @see com.fasterxml.jackson.databind
       *    .JsonDeserializer#deserialize(com.fasterxml.jackson.core.JsonParser, 
       *    com.fasterxml.jackson.databind.DeserializationContext)
       */
      @Override
      public String deserialize(JsonParser jsonParser, DeserializationContext ctx) 
          throws IOException, JsonProcessingException {
            return jsonParser.getValueAsString().trim();
      }
    });
  }
}

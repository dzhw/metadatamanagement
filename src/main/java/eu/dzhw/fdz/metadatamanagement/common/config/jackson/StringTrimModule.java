package eu.dzhw.fdz.metadatamanagement.common.config.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * Trims all String of imported data. 
 * Reference (Code Idea from): {@link https://stackoverflow.com/a/33765854/8080153}
 * 
 * @author Daniel Katzberg
 */
public class StringTrimModule extends SimpleModule {
  
  private static final long serialVersionUID = 1L;
  
  /**
   * Constructor for the StringTrimModule.
   */  
  public StringTrimModule() {
    addDeserializer(String.class, new ScalarDeserializer());
  }  
  
  /**
   * Inner static class for String handling. The inner class trims Strings on import.
   */
  private static class ScalarDeserializer extends StdScalarDeserializer<String> {

    private static final long serialVersionUID = 1L;

    protected ScalarDeserializer() {
      super(String.class);
    }
    
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
  }
}

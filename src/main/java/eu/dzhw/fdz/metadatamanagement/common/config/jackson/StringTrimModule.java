package eu.dzhw.fdz.metadatamanagement.common.config.jackson;

import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.deser.std.StdScalarDeserializer;
import tools.jackson.databind.module.SimpleModule;

/**
 * Trims all String of imported data.
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

    protected ScalarDeserializer() {
      super(String.class);
    }

    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext ctx) {
      return jsonParser.getValueAsString().trim();
    }
  }
}

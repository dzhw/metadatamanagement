package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddiCodebook;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.AllArgsConstructor;

/**
 * General element for a language annotated XML element containing a string,
 * e.g. labels (labl in DDI standard).
 */
@AllArgsConstructor
public class TextElement {

  @JacksonXmlProperty(localName = "xml:lang", isAttribute = true)
  private LanguageEnum lang;

  @JacksonXmlText
  private String value;

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

}

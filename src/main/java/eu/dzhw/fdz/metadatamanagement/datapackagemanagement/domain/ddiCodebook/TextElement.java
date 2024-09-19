package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddiCodebook;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

import lombok.AllArgsConstructor;

/**
 * General element for a language annotated XML element containing a string,
 * e.g. labels (labl in DDI standard).
 */
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.NONE)
public class TextElement {

  public TextElement() {}

  @XmlAttribute(name="xml:lang")
  private LanguageEnum lang;

  @XmlValue
  private String value;

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

}

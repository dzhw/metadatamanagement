package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddicodebook;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import lombok.AllArgsConstructor;

/**
 * Class representing the DDI catgry element.
 */
@AllArgsConstructor
public class Catgry {

  /**
   * Constructor needed by JAXB.
   */
  public Catgry() {}

  @XmlElement(name = "catValu")
  String catValu;

  @XmlElement(name = "labl")
  List<TextElement> labl;

}

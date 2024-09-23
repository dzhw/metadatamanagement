package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddicodebook;

import javax.xml.bind.annotation.XmlElement;

import lombok.AllArgsConstructor;

/**
 * Class representing the DDI stdyDscr element.
 */
@AllArgsConstructor
public class StdyDscr {

  /**
   * Constructor needed by JAXB.
   */
  public StdyDscr() {}

  @XmlElement(name = "citation")
  Citation citation;

}

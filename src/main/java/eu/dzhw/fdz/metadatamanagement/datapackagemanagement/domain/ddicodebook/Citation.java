package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddicodebook;

import javax.xml.bind.annotation.XmlElement;

import lombok.AllArgsConstructor;

/**
 * Class representing the DDI citation element.
 */
@AllArgsConstructor
public class Citation {

  /**
   * Constructor needed by JAXB.
   */
  public Citation() {}

  @XmlElement(name = "titlStmt")
  TitlStmt titlStmt;

}

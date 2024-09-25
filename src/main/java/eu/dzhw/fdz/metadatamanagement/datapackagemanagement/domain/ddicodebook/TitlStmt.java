package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddicodebook;

import javax.xml.bind.annotation.XmlElement;

import lombok.AllArgsConstructor;

/**
 * Class representing the DDI titlStmt element.
 */
@AllArgsConstructor
public class TitlStmt {

  /**
   * Constructor needed by JAXB.
   */
  public TitlStmt() {}

  @XmlElement(name = "titl")
  TextElement titl;

  @XmlElement(name = "parTitl")
  TextElement parTitle;

}

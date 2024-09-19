package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddiCodebook;

import javax.xml.bind.annotation.XmlElement;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TitlStmt {

  public TitlStmt() {}

  @XmlElement(name="titl")
  TextElement titl;

  @XmlElement(name="parTitl")
  TextElement parTitle;

}

package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddiCodebook;

import javax.xml.bind.annotation.XmlElement;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Citation {

  public Citation() {}

  @XmlElement(name="titlStmt")
  TitlStmt titlStmt;

}

package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddicodebook;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import lombok.AllArgsConstructor;

/**
 * Class representing the DDI dataDscr element.
 */
@AllArgsConstructor
public class DataDscr {

  /**
   * Constructor needed by JAXB.
   */
  public DataDscr() {}

  @XmlElement(name = "var")
  List<Var> var;

}

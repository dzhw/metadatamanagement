package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddicodebook;

import javax.xml.bind.annotation.XmlElement;

import lombok.AllArgsConstructor;

/**
 * Class representing the DDI fileTxt element.
 */
@AllArgsConstructor
public class FileTxt {

  /**
   * Constructor needed by JAXB.
   */
  public FileTxt() {}

  @XmlElement(name = "fileName")
  String fileName;

  @XmlElement(name = "fileCont")
  TextElement fileCont;

}

package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddicodebook;

import javax.xml.bind.annotation.XmlElement;

import lombok.AllArgsConstructor;

import java.util.List;

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
  List<TextElement> fileName;

  @XmlElement(name = "fileCont")
  List<TextElement> fileCont;

}

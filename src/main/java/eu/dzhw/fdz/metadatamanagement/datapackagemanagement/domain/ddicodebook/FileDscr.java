package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddicodebook;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;

import lombok.AllArgsConstructor;

/**
 * Class representing the DDI Codebook fileDscr element.
 */
@AllArgsConstructor
public class FileDscr {

  public FileDscr() {}

  @XmlAttribute(name = "ID")
  String id;

  @XmlElement(name = "fileTxt")
  FileTxt fileTxt;
}

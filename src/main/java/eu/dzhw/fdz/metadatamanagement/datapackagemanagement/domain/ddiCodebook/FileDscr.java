package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddiCodebook;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FileDscr {

  public FileDscr() {}

  @XmlAttribute(name="ID")
  String id;

  @XmlElement(name="fileTxt")
  FileTxt fileTxt;
}

package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddiCodebook;

import javax.xml.bind.annotation.XmlElement;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FileTxt {

  public FileTxt() {}

  @XmlElement(name="fileName")
  String fileName;

  @XmlElement(name="fileCont")
  TextElement fileCont;

}

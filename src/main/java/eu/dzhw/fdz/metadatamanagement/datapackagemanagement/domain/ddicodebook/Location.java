package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddicodebook;

import lombok.AllArgsConstructor;

import javax.xml.bind.annotation.XmlAttribute;

@AllArgsConstructor
public class Location {

  @XmlAttribute(name = "fileid")
  String fileId;
}

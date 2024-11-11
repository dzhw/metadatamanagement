package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddicodebook;

import lombok.AllArgsConstructor;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * A wrapper element for the dataset ID a variable is linked to.
 */
@AllArgsConstructor
public class Location {

  /**
   * The dataset ID a variable is linked to (e.g. 'project-ds1').
   */
  @XmlAttribute(name = "fileid")
  String fileId;
}

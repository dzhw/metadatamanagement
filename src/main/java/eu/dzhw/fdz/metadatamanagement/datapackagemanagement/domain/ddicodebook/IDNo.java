package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddicodebook;

import lombok.AllArgsConstructor;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

@AllArgsConstructor
public class IDNo {

  public IDNo() {}

  /**
   * e.g. 'DOI'
   */
  @XmlAttribute
  String agency;

  /**
   * The data package version e.g. '1.0.0'
   */
  @XmlAttribute
  String elementVersion;

  @XmlValue
  String dataPackageDOI;
}

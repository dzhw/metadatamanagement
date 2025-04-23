package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddicodebook;

import lombok.AllArgsConstructor;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

/**
 * ID element for the study (e.g. DOI).
 */
@AllArgsConstructor
public class IdNo {

  public IdNo() {}

  /**
   * The agency the ID is registered with (e.g. 'DOI').
   */
  @XmlAttribute
  String agency;

  /**
   * The data package version (e.g. '1.0.0').
   */
  @XmlAttribute
  String elementVersion;

  /**
   * The ID value.
   */
  @XmlValue
  String value;
}

package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddiCodebook;

import javax.xml.bind.annotation.XmlElement;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class StdyDscr {

  public StdyDscr() {}

  @XmlElement(name="citation")
  Citation citation;

}

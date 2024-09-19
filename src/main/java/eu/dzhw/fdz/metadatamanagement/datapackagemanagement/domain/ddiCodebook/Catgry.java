package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddiCodebook;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Catgry {

  public Catgry() {}

  @XmlElement(name="catValu")
  String catValu;

  @XmlElement(name="labl")
  List<TextElement> labl;

}

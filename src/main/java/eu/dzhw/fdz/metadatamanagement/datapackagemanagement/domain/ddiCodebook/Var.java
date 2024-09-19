package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddiCodebook;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Var {

  public Var() {}

  @XmlAttribute(name="name")
  String name;

  @XmlAttribute(name="files")
  String files;

  @XmlElement(name="labl")
  List<TextElement> labl;

  @XmlElement(name="qstn")
  List<TextElement> qstn;

  @XmlElement(name="catgry")
  List<Catgry> catgry;

}

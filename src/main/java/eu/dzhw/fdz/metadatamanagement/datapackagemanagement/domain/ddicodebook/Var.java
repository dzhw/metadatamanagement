package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddicodebook;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import lombok.AllArgsConstructor;

/**
 * Class representing the DDI Codebook var element.
 */
@AllArgsConstructor
public class Var {

  /**
   * Constructor needed by JAXB.
   */
  public Var() {}

  @XmlAttribute(name = "name")
  String name;

  @XmlAttribute(name = "files")
  String files;

  @XmlElement(name = "labl")
  List<TextElement> labl;

  @XmlElement(name = "qstn")
  List<TextElement> qstn;

  @XmlElement(name = "txt")
  List<TextElement> txt;

  @XmlElement(name = "catgry")
  List<Catgry> catgry;

}

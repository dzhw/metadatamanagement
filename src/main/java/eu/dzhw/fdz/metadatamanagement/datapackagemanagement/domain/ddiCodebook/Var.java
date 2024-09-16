package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddiCodebook;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Var {

  @JacksonXmlProperty(localName = "name", isAttribute = true)
  String name;

  @JacksonXmlProperty(localName = "files", isAttribute = true)
  String files;

  @JacksonXmlProperty(localName = "labl")
  List<TextElement> labl;

  @JacksonXmlProperty(localName = "qstn")
  List<TextElement> qstn;

  @JacksonXmlProperty(localName = "catgry")
  List<Catgry> catgry;

}

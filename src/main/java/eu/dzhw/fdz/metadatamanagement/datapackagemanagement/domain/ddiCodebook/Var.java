package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddiCodebook;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Var {

  @JacksonXmlProperty(localName = "name", isAttribute = true)
  String name;

  @JacksonXmlProperty(localName = "files", isAttribute = true)
  String files;

  @JacksonXmlProperty(localName = "labl")
  @JacksonXmlElementWrapper(useWrapping = false)
  List<TextElement> labl;

  @JacksonXmlProperty(localName = "qstn")
  @JacksonXmlElementWrapper(useWrapping = false)
  List<TextElement> qstn;

  @JacksonXmlProperty(localName = "catgry")
  @JacksonXmlElementWrapper(useWrapping = false)
  List<Catgry> catgry;

}

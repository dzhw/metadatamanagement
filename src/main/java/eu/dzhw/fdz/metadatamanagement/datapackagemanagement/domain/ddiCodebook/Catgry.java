package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddiCodebook;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Catgry {

  @JacksonXmlProperty(localName = "catValu")
  String catValu;

  @JacksonXmlProperty(localName = "labl")
  @JacksonXmlElementWrapper(useWrapping = false)
  List<TextElement> labl;

}

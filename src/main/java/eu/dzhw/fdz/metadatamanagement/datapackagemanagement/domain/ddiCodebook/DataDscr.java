package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddiCodebook;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DataDscr {

  @JacksonXmlProperty(localName = "var")
  @JacksonXmlElementWrapper(useWrapping = false)
  List<Var> var;

}

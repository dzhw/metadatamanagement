package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddiCodebook;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TitlStmt {

  @JacksonXmlProperty(localName = "titl")
  private TextElement titl;

  @JacksonXmlProperty(localName = "parTitl")
  private TextElement parTitle;

}

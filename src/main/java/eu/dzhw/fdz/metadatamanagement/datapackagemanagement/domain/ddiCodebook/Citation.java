package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddiCodebook;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Citation {

  @JacksonXmlProperty(localName = "titlStmt")
  private TitlStmt titlStmt;

}

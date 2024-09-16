package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddiCodebook;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FileDscr {

  @JacksonXmlProperty(localName = "ID", isAttribute = true)
  String id;

  @JacksonXmlProperty(localName = "fileTxt")
  FileTxt fileTxt;
}

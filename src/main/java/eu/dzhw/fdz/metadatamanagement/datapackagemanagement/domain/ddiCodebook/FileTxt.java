package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddiCodebook;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FileTxt {

  @JacksonXmlProperty(localName = "fileName")
  String fileName;

  @JacksonXmlProperty(localName = "fileCont")
  TextElement fileCont;

}

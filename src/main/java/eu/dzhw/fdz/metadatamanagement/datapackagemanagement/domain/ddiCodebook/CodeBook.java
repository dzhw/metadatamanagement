package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddiCodebook;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;

/**
 * Record defining the mapping of the DDI Codebook standard.
 */
@AllArgsConstructor
@JacksonXmlRootElement(namespace = "ddi:codebook:2_5", localName = "codeBook")
public class CodeBook {

  @JacksonXmlProperty(localName = "stdyDscr")
  private StdyDscr stdyDscr;

  @JacksonXmlProperty(localName = "fileDscr")
  private List<FileDscr> fileDscr;

  @JacksonXmlProperty(localName = "dataDscr")
  private DataDscr dataDscr;

}

package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddiCodebook;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;

/**
 * Defines the mapping of the DDI Codebook standard.
 */
@Getter
@Setter
@JacksonXmlRootElement(localName = "codeBook", namespace = "ddi:codebook:2_5")
public class CodeBook {

  public CodeBook(StdyDscr stdyDscr, List<FileDscr> fileDscr, DataDscr dataDscr) {
    this.stdyDscr = stdyDscr;
    this.fileDscr = fileDscr;
    this.dataDscr = dataDscr;
  }

//  @JacksonXmlProperty(localName = "xmlns", isAttribute = true)
//  private String nameSpace = "ddi:codebook:2_5";

//  @JacksonXmlProperty(localName = "xmlns:xsi", isAttribute = true)
//  private String xsi = "http://www.w3.org/2001/XMLSchema-instance";
//
//  @JacksonXmlProperty(localName = "xmlns:xs", isAttribute = true)
//  private String xs = "http://www.w3.org/2001/XMLSchema";
//
//  @JacksonXmlProperty(localName = "xsi:schemaLocation", isAttribute = true)
//  private String schemaLocation = "http://www.ddialliance.org/Specification/DDI-Codebook/2.5/XMLSchema/codebook.xsd";

  @JacksonXmlProperty(localName = "stdyDscr")
  private StdyDscr stdyDscr;

  @JacksonXmlProperty(localName = "fileDscr")
  @JacksonXmlElementWrapper(useWrapping = false)
  private List<FileDscr> fileDscr;

  @JacksonXmlProperty(localName = "dataDscr")
  private DataDscr dataDscr;

}

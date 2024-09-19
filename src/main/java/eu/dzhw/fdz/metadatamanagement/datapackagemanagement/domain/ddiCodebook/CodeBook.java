package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddiCodebook;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Defines the mapping of the DDI Codebook standard.
 */

@XmlRootElement(name="codeBook")
public class CodeBook {

  public CodeBook(StdyDscr stdyDscr, List<FileDscr> fileDscr, DataDscr dataDscr) {
    this.stdyDscr = stdyDscr;
    this.fileDscr = fileDscr;
    this.dataDscr = dataDscr;
  }

  public  CodeBook() {}

  @XmlElement(name="stdyDscr")
  private StdyDscr stdyDscr;

  @XmlElement(name="fileDscr")
  private List<FileDscr> fileDscr;

  @XmlElement(name="dataDscr")
  private DataDscr dataDscr;

}

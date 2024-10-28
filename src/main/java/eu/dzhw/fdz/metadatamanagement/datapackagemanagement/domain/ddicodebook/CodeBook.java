package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddicodebook;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class representing the root element codebook of the mapping of the DDI Codebook standard.
 */

@XmlRootElement(name = "codeBook")
public class CodeBook {

  /**
   * Constructor for codebook.
   * @param stdyDscr the data package description in form of the DDI stdyDscr element.
   * @param fileDscr the dataset metadata in form of the DDI fileDscr element.
   * @param dataDscr the variable metadata in form of the DDI dataDscr element.
   */
  public CodeBook(StdyDscr stdyDscr, List<FileDscr> fileDscr, DataDscr dataDscr) {
    this.stdyDscr = stdyDscr;
    this.fileDscr = fileDscr;
    this.dataDscr = dataDscr;
  }

  /**
   * Constructor needed by JAXB.
   */
  public  CodeBook() {}

  @XmlElement(name = "stdyDscr")
  private StdyDscr stdyDscr;

  @XmlElement(name = "fileDscr")
  private List<FileDscr> fileDscr;

  @XmlElement(name = "dataDscr")
  private DataDscr dataDscr;

}

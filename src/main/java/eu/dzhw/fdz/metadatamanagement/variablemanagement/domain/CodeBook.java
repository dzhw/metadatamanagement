package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * Record defining the mapping of the DDI Codebook standard.
 */
@JacksonXmlRootElement(namespace = "ddi:codebook:2_5", localName = "codeBook")
public class CodeBook {
  public CodeBook(StdyDscr stdyDscr, DataDscr dataDscr) {
    this.stdyDscr = stdyDscr;
    this.dataDscr = dataDscr;
  }

  @JacksonXmlProperty(localName = "stdyDscr")
  private StdyDscr stdyDscr;
  @JacksonXmlProperty(localName = "dataDscr")
  private DataDscr dataDscr;

  public static class StdyDscr {
    public StdyDscr(Citation citation) {
      this.citation = citation;
    }

    @JacksonXmlProperty(localName = "citation")
    Citation citation;

    public static class Citation {
      public Citation(TitlStmt titlStmt) {
        this.titlStmt = titlStmt;
      }

      @JacksonXmlProperty(localName = "titlStmt")
      private TitlStmt titlStmt;

      public static class TitlStmt {
        public TitlStmt(String titl) {
          this.titl = titl;
        }

        @JacksonXmlProperty(localName = "titl")
        private String titl;
      }
    }
  }

  public static class DataDscr {
    public DataDscr(List<DataDscr.Var> var) {
      this.var = var;
    }

    @JacksonXmlProperty(localName = "var")
    List<Var> var;

    public static class Var {
      public Var(String name) {
        this.name = name;
      }
      @JacksonXmlProperty(localName = "name", isAttribute = true)
      String name;
    }
  }
}

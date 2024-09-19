package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddiCodebook;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DataDscr {

  public DataDscr() {}

  @XmlElement(name="var")
  List<Var> var;

}

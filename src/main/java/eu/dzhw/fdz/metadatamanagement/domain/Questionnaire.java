package eu.dzhw.fdz.metadatamanagement.domain;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import eu.dzhw.fdz.metadatamanagement.domain.validation.DataAcquisitionProjectExists;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * A Questionnaire.
 * 
 * @author Daniel Katzberg
 *
 */
@Document(collection = "surveys")
@GeneratePojoBuilder(intoPackage = "eu.dzhw.fdz.metadatamanagement.domain.builders")
public class Questionnaire {

  @Id
  @NotEmpty
  private String id;

  @NotEmpty
  @DataAcquisitionProjectExists
  private String dataAcquisitionProjectId;

  /* GETTER / SETTER */
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getDataAcquisitionProjectId() {
    return dataAcquisitionProjectId;
  }

  public void setDataAcquisitionProjectId(String dataAcquisitionProjectId) {
    this.dataAcquisitionProjectId = dataAcquisitionProjectId;
  }
}

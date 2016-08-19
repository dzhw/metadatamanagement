package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * A Instrument.
 * 
 * @author Daniel Katzberg
 *
 */
@Document(collection = "instruments")
@GeneratePojoBuilder(
    intoPackage = "eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.builders")
public class Instrument extends AbstractRdcDomainObject {

  @Id
  @NotEmpty(message = "{error.instrument.id.not-empty}")
  private String id;

  @NotEmpty(message = "{error.instrument.data-acquisition-project.id.not-empty}")
  private String dataAcquisitionProjectId;
  
  @NotEmpty(message = "{error.instrument.survey.id.not-empty}")
  private String surveyId;

  /*
   * (non-Javadoc)
   * 
   * @see eu.dzhw.fdz.metadatamanagement.domain.AbstractRdcDomainObject#getId()
   */
  @Override
  public String getId() {
    return id;
  }

  /* GETTER / SETTER */
  public void setId(String id) {
    this.id = id;
  }

  public String getDataAcquisitionProjectId() {
    return dataAcquisitionProjectId;
  }

  public void setDataAcquisitionProjectId(String dataAcquisitionProjectId) {
    this.dataAcquisitionProjectId = dataAcquisitionProjectId;
  }

  public String getSurveyId() {
    return surveyId;
  }

  public void setSurveyId(String surveyId) {
    this.surveyId = surveyId;
  }
}

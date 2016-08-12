package eu.dzhw.fdz.metadatamanagement.questionnairemanagement.domain;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * A Questionnaire.
 * 
 * @author Daniel Katzberg
 *
 */
@Document(collection = "questionnaires")
@GeneratePojoBuilder(
    intoPackage = "eu.dzhw.fdz.metadatamanagement.questionnairemanagement.domain.builders")
public class Questionnaire extends AbstractRdcDomainObject {

  @Id
  @NotEmpty(message = "{error.questionnaire.id.not-empty}")
  private String id;

  @NotEmpty(message = "{error.questionnaire.dataAcquisitionProject.id.not-empty}")
  private String dataAcquisitionProjectId;
  
  @NotEmpty(message = "{error.questionnaire.survey.id.not-empty}")
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

package eu.dzhw.fdz.metadatamanagement.domain;

import org.hibernate.validator.constraints.NotEmpty;

import com.google.common.base.MoreObjects;

import eu.dzhw.fdz.metadatamanagement.domain.validation.DataAcquisitionProjectExists;
import eu.dzhw.fdz.metadatamanagement.domain.validation.SurveyExists;
import eu.dzhw.fdz.metadatamanagement.domain.validation.SurveyHasSameDataAcquisitionProject;

/**
 * This interface is for domain object, which has a reference of the data acquisition project and a
 * survey. This Interface is needed for the {@link SurveyHasSameDataAcquisitionProject} Annotation /
 * Validation.
 * 
 * @author Daniel Katzberg
 *
 */
public abstract class AbstractRdcDomainObjectWithProjectSurvey extends AbstractRdcDomainObject {

  @NotEmpty
  @DataAcquisitionProjectExists
  private String dataAcquisitionProjectId;

  @SurveyExists
  private String surveyId;

  /*
   * (non-Javadoc)
   * 
   * @see eu.dzhw.fdz.metadatamanagement.domain.AbstractRdcDomainObject#toString()
   */
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .add("super", super.toString())
      .add("dataAcquisitionProjectId", dataAcquisitionProjectId)
      .add("surveyId", surveyId)
      .toString();
  }

  /* GETTER / SETTER */
  public String getSurveyId() {
    return surveyId;
  }

  public void setSurveyId(String surveyId) {
    this.surveyId = surveyId;
  }

  public String getDataAcquisitionProjectId() {
    return dataAcquisitionProjectId;
  }

  public void setDataAcquisitionProjectId(String dataAcquisitionProjectId) {
    this.dataAcquisitionProjectId = dataAcquisitionProjectId;
  }

}

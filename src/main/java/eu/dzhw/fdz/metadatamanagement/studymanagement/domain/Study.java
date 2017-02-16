package eu.dzhw.fdz.metadatamanagement.studymanagement.domain;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.base.MoreObjects;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.validation.ValidDataAvailability;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.validation.ValidStudyId;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.validation.ValidSurveyDesign;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.ValidAccessWays;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * The study domain object represents a study. A study can has more than one release. 
 * Every {@link DataAcquisitionProject} has exact one Study.
 * 
 * @author Daniel Katzberg
 *
 */
@Document(collection = "studies")
@GeneratePojoBuilder(
     intoPackage = "eu.dzhw.fdz.metadatamanagement.studymanagement.domain.builders")
@ValidStudyId(message = "study-management.error.study.id.not-valid-id") 
public class Study extends StudySubDocument {
  
  @I18nStringSize(max = StringLengths.LARGE,
      message = "study-management.error.study.citation-hint.i18n-string-size")
  private I18nString citationHint;
  
  @NotNull(message = "study-management.error.study.data-availability.not-null")
  @ValidDataAvailability(
      message = "study-management.error.study.data-availability.valid-data-availability")
  private I18nString dataAvailability;
  
  @NotNull(message = "study-management.error.study.survey-design.not-null")
  @ValidSurveyDesign(
      message = "study-management.error.study.survey-design.valid-survey-design")
  private I18nString surveyDesign;
  
  @NotEmpty(message = "study-management.error.study.access-ways.not-empty")
  @ValidAccessWays(
      message = "study-management.error.study.access-ways.valid-access-ways")
  private List<String> accessWays;

  /* Nested Objects */
  @Valid
  private List<Release> releases;
  
  public Study() {
    super();
  }
  
  public Study(Study study) {
    super();
    BeanUtils.copyProperties(study, this);
  }
  
  /*
   * (non-Javadoc)
   * @see eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject#toString()
   */
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .add("super", super.toString())
      .add("id", id)
      .add("title", title)
      .add("description", description)
      .add("institution", institution)
      .add("surveySeries", surveySeries)
      .add("sponsor", sponsor)
      .add("citationHint", citationHint)
      .add("authors", authors)
      .add("dataAvailability", dataAvailability)
      .add("surveyDesign", surveyDesign)
      .add("accessWays", accessWays)
      .add("releases", releases)
      .add("dataAcquisitionProjectId", dataAcquisitionProjectId)
      .toString();
  }


  public List<Release> getReleases() {
    return releases;
  }

  public void setReleases(List<Release> releases) {
    this.releases = releases;
  }
  
  public I18nString getCitationHint() {
    return citationHint;
  }

  public void setCitationHint(I18nString citationHint) {
    this.citationHint = citationHint;
  }

  public List<String> getAccessWays() {
    return accessWays;
  }

  public void setAccessWays(List<String> accessWays) {
    this.accessWays = accessWays;
  }

  public I18nString getDataAvailability() {
    return dataAvailability;
  }

  public void setDataAvailability(I18nString dataAvailability) {
    this.dataAvailability = dataAvailability;
  }

  public I18nString getSurveyDesign() {
    return surveyDesign;
  }

  public void setSurveyDesign(I18nString surveyDesign) {
    this.surveyDesign = surveyDesign;
  }
}

package eu.dzhw.fdz.metadatamanagement.studymanagement.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.util.Patterns;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringNotEmpty;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import io.searchbox.annotations.JestId;

/**
 * Subset of study attributes which can be used in other search documents
 * as sub document.
 * @author René Reitmann
 */
public class StudySubDocument extends AbstractRdcDomainObject {

  @Id
  @JestId
  @NotEmpty(message = "study-management.error.study.id.not-empty")
  @Size(max = StringLengths.MEDIUM, message = "study-management.error.study.id.size")
  @Pattern(regexp = Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS_AND_EXCLAMATIONMARK,
      message = "study-management.error.study.id.pattern")
  protected String id;
  
  @Indexed
  @NotEmpty(message = "study-management.error.study.data-acquisition-project.id.not-empty")
  protected String dataAcquisitionProjectId;
  
  @NotNull(message = "study-management.error.study.title.not-null")
  @I18nStringSize(max = StringLengths.LARGE,
      message = "study-management.error.study.title.i18n-string-size")
  @I18nStringNotEmpty(message = "study-management.error.study.title.i18n-string-not-empty")
  protected I18nString title;
  
  @NotNull(message = "study-management.error.study.description.not-null")
  @I18nStringSize(max = StringLengths.LARGE,
      message = "study-management.error.study.description.i18n-string-size")
  @I18nStringNotEmpty(message = "study-management.error.study.description.i18n-string-not-empty")
  protected I18nString description;
  
  @NotNull(message = "study-management.error.study.institution.not-null")
  @I18nStringSize(max = StringLengths.MEDIUM,
      message = "study-management.error.study.institution.i18n-string-size")
  @I18nStringNotEmpty(message = "study-management.error.study.institution.i18n-string-not-empty")
  protected I18nString institution;
  
  @I18nStringSize(max = StringLengths.MEDIUM,
      message = "study-management.error.study.survey-series.i18n-string-size")
  protected I18nString surveySeries;
  
  @I18nStringSize(max = StringLengths.MEDIUM,
      message = "study-management.error.study.sponsor.i18n-string-size")
  protected I18nString sponsor;
  
  @Size(max = StringLengths.MEDIUM,
      message = "study-management.error.study.authors.i18n-string-size")
  protected String authors;

  public StudySubDocument() {
    super();
  }
  
  public StudySubDocument(Study study) {
    super();
    BeanUtils.copyProperties(study, this);
  }

  @Override
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

  public I18nString getSurveySeries() {
    return surveySeries;
  }

  public void setSurveySeries(I18nString surveySeries) {
    this.surveySeries = surveySeries;
  }

  public I18nString getTitle() {
    return title;
  }

  public void setTitle(I18nString title) {
    this.title = title;
  }

  public I18nString getDescription() {
    return description;
  }

  public void setDescription(I18nString description) {
    this.description = description;
  }

  public I18nString getInstitution() {
    return institution;
  }

  public void setInstitution(I18nString institution) {
    this.institution = institution;
  }

  public I18nString getSponsor() {
    return sponsor;
  }

  public void setSponsor(I18nString sponsor) {
    this.sponsor = sponsor;
  }

  public String getAuthors() {
    return authors;
  }

  public void setAuthors(String authors) {
    this.authors = authors;
  }

}

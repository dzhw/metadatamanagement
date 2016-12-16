package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.base.MoreObjects;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.util.Patterns;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.validation.ValidInstrumentIdPattern;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.validation.ValidInstrumentType;
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
@ValidInstrumentIdPattern(message = "instrument-management.error"
    + ".instrument.valid-instrument-id-pattern")
public class Instrument extends AbstractRdcDomainObject {

  @Id
  @NotEmpty(message = "instrument-management.error.instrument.id.not-empty")
  @Pattern(regexp = Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS,
      message = "instrument-management.error.instrument.id.pattern")
  @Size(max = StringLengths.MEDIUM, message = "instrument-management.error.instrument.id.size")
  private String id;
  
  @NotNull(message = "instrument-management.error.instrument.title.not-null")
  @I18nStringSize(max = StringLengths.MEDIUM, min = 1,
      message = "instrument-management.error.instrument.title.i18n-string-size")
  private I18nString title;
  
  @NotNull(message = "instrument-management.error.instrument.description.not-null")
  @I18nStringSize(max = StringLengths.MEDIUM, min = 1,
      message = "instrument-management.error.instrument.description.i18n-string-size")
  private I18nString description;
  
  @NotEmpty(message = "instrument-management.error.instrument.type.not-empty")
  @ValidInstrumentType(message = "instrument-management.error.instrument.type.valid")
  private String type;

  @NotEmpty(message = 
      "instrument-management.error.instrument.data-acquisition-project-id.not-empty")
  private String dataAcquisitionProjectId;
  
  @NotEmpty(message = "instrument-management.error.instrument.survey-id.not-empty")
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

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
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

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .add("super", super.toString())
      .add("id", id)
      .add("title", title)
      .add("description", description)
      .add("type", type)
      .add("dataAcquisitionProjectId", dataAcquisitionProjectId)
      .add("surveyId", surveyId)
      .toString();
  } 
}

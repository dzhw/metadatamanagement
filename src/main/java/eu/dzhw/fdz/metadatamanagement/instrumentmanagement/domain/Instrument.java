package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.base.MoreObjects;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.util.Patterns;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringNotEmpty;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.validation.ValidInstrumentIdPattern;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.validation.ValidInstrumentType;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.validation.ValidUniqueInstrumentNumber;
import io.searchbox.annotations.JestId;
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
@ValidUniqueInstrumentNumber(message = "instrument-management.error"
    + ".instrument.unique-instrument-number")
@CompoundIndex(def = "{number: 1, dataAcquisitionProjectId: 1}", unique = true)
public class Instrument extends AbstractRdcDomainObject {
 
  @Id
  @JestId
  @NotEmpty(message = "instrument-management.error.instrument.id.not-empty")
  @Pattern(regexp = Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS_AND_EXCLAMATIONMARK,
      message = "instrument-management.error.instrument.id.pattern")
  @Size(max = StringLengths.MEDIUM, message = "instrument-management.error.instrument.id.size")
  private String id;
  
  @Indexed
  @NotEmpty(message = 
      "instrument-management.error.instrument.data-acquisition-project-id.not-empty")
  private String dataAcquisitionProjectId;
  
  @NotNull(message = "instrument-management.error.instrument.title.not-null")
  @I18nStringSize(max = StringLengths.MEDIUM, min = 1,
      message = "instrument-management.error.instrument.title.i18n-string-size")
  @I18nStringNotEmpty(message = "instrument-management.error.instrument.title."
      + "i18n-string-not-emty")
  private I18nString title;
  
  @NotNull(message = "instrument-management.error.instrument.description.not-null")
  @I18nStringSize(max = StringLengths.MEDIUM, min = 1,
      message = "instrument-management.error.instrument.description.i18n-string-size")
  @I18nStringNotEmpty(message = "instrument-management.error.instrument.description."
      + "i18n-string-not-emty")
  private I18nString description;
  
  @NotNull(message = "instrument-management.error.instrument.number.not-null")
  private Integer number;

  @NotEmpty(message = "instrument-management.error.instrument.type.not-empty")
  @ValidInstrumentType(message = "instrument-management.error.instrument.type.valid")
  private String type;
  
  @Indexed
  private List<String> surveyIds;
  
  @NotEmpty(message = "instrument-management.error.instrument.survey-numbers.not-empty")
  private List<Integer> surveyNumbers;
  
  @Indexed
  @NotEmpty(message = "instrument-management.error.instrument.study-id.not-empty")
  private String studyId;
  
  public Instrument() {
    super();
  }
  
  public Instrument(Instrument instrument) {
    super();
    BeanUtils.copyProperties(instrument, this);
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

  public Integer getNumber() {
    return number;
  }

  public void setNumber(Integer number) {
    this.number = number;
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
      .add("type", type)
      .add("dataAcquisitionProjectId", dataAcquisitionProjectId)
      .add("surveyIds", surveyIds)
      .add("surveyNumbers", surveyNumbers)
      .add("number", number)
      .add("studyId", studyId)
      .toString();
  }


  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public List<String> getSurveyIds() {
    return surveyIds;
  }

  public void setSurveyIds(List<String> surveyIds) {
    this.surveyIds = surveyIds;
  }

  public List<Integer> getSurveyNumbers() {
    return surveyNumbers;
  }

  public void setSurveyNumbers(List<Integer> surveyNumbers) {
    this.surveyNumbers = surveyNumbers;
  }

  public String getStudyId() {
    return studyId;
  }

  public void setStudyId(String studyId) {
    this.studyId = studyId;
  }
}

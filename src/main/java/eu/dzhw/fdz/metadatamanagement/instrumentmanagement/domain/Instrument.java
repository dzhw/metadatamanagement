package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain;

import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.base.MoreObjects;

import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.validation.ValidInstrumentIdPattern;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.validation.ValidInstrumentType;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.validation.ValidUniqueInstrumentNumber;
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
public class Instrument extends InstrumentSubDocument {

  @NotEmpty(message = "instrument-management.error.instrument.type.not-empty")
  @ValidInstrumentType(message = "instrument-management.error.instrument.type.valid")
  private String type;
  
  private List<String> surveyIds;
  
  @NotEmpty(message = "instrument-management.error.instrument.survey-numbers.not-empty")
  private List<Integer> surveyNumbers;
  
  private String studyId;
  
  public Instrument() {
    super();
  }
  
  public Instrument(Instrument instrument) {
    super();
    BeanUtils.copyProperties(instrument, this);
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

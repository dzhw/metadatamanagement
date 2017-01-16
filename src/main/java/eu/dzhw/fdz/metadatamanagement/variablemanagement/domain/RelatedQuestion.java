package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.google.common.base.MoreObjects;

import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * The related Question includes the references to the question and instrument. This is a sub 
 * element by the variable.
 * 
 * @author Daniel Katzberg
 *
 */
@GeneratePojoBuilder(
    intoPackage = "eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.builders")
public class RelatedQuestion {

  /* Domain Object listed attributes */
  @NotEmpty(message = "variable-management.error.variable."
      + "related-question-instrument-number.not-empty")
  private String instrumentNumber;
  
  @Size(max = StringLengths.SMALL,
      message = "variable-management.error.variable."
      + "related-question-number.size")
  @NotEmpty(message = "variable-management.error.variable."
      + "related-question-number.not-empty")
  private String questionNumber;
  
  /* Nested Objects */
  
  /* Foreign Keys */
  private String questionId;
  
  private String instrumentId;

  
  /*
   * (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .add("instrumentNumber", instrumentNumber)
      .add("questionNumber", questionNumber)
      .add("questionId", questionId)
      .add("instrumentId", instrumentId)
      .toString();
  }

  /* GETTER / SETTER */
  public String getInstrumentNumber() {
    return instrumentNumber;
  }  
  
  public void setInstrumentNumber(String instrumentNumber) {
    this.instrumentNumber = instrumentNumber;
  }

  public String getQuestionNumber() {
    return questionNumber;
  }

  public void setQuestionNumber(String questionNumber) {
    this.questionNumber = questionNumber;
  }

  public String getQuestionId() {
    return questionId;
  }

  public void setQuestionId(String questionId) {
    this.questionId = questionId;
  }

  public String getInstrumentId() {
    return instrumentId;
  }

  public void setInstrumentId(String instrumentId) {
    this.instrumentId = instrumentId;
  }
}

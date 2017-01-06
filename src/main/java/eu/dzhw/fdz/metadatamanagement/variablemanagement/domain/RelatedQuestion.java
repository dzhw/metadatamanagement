package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain;

import com.google.common.base.MoreObjects;

/**
 * The related Question includes the references to the question and instrument. This is a sub 
 * element by the variable.
 * 
 * @author Daniel Katzberg
 *
 */
public class RelatedQuestion {

  /* Domain Object listed attributes */
  private String instrumentNumber;
  
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

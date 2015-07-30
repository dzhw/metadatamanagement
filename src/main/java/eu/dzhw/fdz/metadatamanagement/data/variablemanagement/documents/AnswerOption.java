package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * The answer option represents the options of answer for closed questions.
 * The answer option bases on two elements: the answer code itself and a label.
 * 
 * @author Daniel Katzberg
 *
 */
public class AnswerOption {
  
  /**
   * This is the code of an answer. It is internal code representation of the answer.
   */
  @Size(max = 60)
  private String code;
  
  /**
   * The Label is the answer which are read by the subjects.
   */
  @Size(max = 60)
  @NotEmpty
  private String label;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }
  
}

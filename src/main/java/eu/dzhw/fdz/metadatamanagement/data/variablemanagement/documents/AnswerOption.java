package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * The answer option represents the options of answer for closed questions. The answer option bases
 * on two elements: the answer code itself and a label.
 * 
 * @author Daniel Katzberg
 *
 */
public class AnswerOption {

  public static final String CODE_FIELD = "code";
  public static final String LABEL_FIELD = "label";

  /**
   * This is the code of an answer. It is internal code representation of the answer.
   */
  @NotNull
  private Integer code;

  /**
   * The Label is the answer which are read by the subjects.
   */
  @Size(max = 60)
  private String label;

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

}

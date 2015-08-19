package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * The answer option represents the options of answer for closed questions. The answer option bases
 * on two elements: the answer code itself and a label.
 * 
 * @author Daniel Katzberg
 *
 */
@GeneratePojoBuilder
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

  /*
   * (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.code == null) ? 0 : this.code.hashCode());
    result = prime * result + ((this.label == null) ? 0 : this.label.hashCode());
    return result;
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    AnswerOption other = (AnswerOption) obj;
    if (this.code == null) {
      if (other.code != null) {
        return false;
      }
    } else if (!this.code.equals(other.code)) {
      return false;
    }
    if (this.label == null) {
      if (other.label != null) {
        return false;
      }
    } else if (!this.label.equals(other.label)) {
      return false;
    }
    return true;
  }
}

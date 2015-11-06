package eu.dzhw.fdz.metadatamanagement.web.rest.dto;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import eu.dzhw.fdz.metadatamanagement.domain.enumeration.DataTypes;
import eu.dzhw.fdz.metadatamanagement.domain.enumeration.ScaleLevel;

/**
 * A DTO for the Variable entity.
 */
public class VariableDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;

  @NotNull
  @Size(max = 256)
  private String name;

  @NotNull
  private DataTypes dataType;

  private String label;

  @Size(max = 256)
  private String question;

  @NotNull
  private ScaleLevel scaleLevel;

  private Long surveyId;

  private String surveyTitle;

  

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
  
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public DataTypes getDataType() {
    return dataType;
  }

  public void setDataType(DataTypes dataType) {
    this.dataType = dataType;
  }
  
  public String getQuestion() {
    return question;
  }

  public void setQuestion(String question) {
    this.question = question;
  }
  
  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public ScaleLevel getScaleLevel() {
    return scaleLevel;
  }

  public void setScaleLevel(ScaleLevel scaleLevel) {
    this.scaleLevel = scaleLevel;
  }

  public String getSurveyTitle() {
    return surveyTitle;
  }

  public void setSurveyTitle(String surveyTitle) {
    this.surveyTitle = surveyTitle;
  }
  
  public Long getSurveyId() {
    return surveyId;
  }

  public void setSurveyId(Long surveyId) {
    this.surveyId = surveyId;
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }

    VariableDto variableDto = (VariableDto) object;

    if (!Objects.equals(id, variableDto.id)) {
      return false;
    }  

    return true;
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "VariableDTO{" + "id=" + id + ", name='" + name + "'" + ", dataType='" + dataType + "'"
        + ", label='" + label + "'" + ", question='" + question + "'" + ", scaleLevel='"
        + scaleLevel + "'" + '}';
  }
}

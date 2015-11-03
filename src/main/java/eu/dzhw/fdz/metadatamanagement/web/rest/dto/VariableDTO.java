package eu.dzhw.fdz.metadatamanagement.web.rest.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import eu.dzhw.fdz.metadatamanagement.domain.enumeration.DataTypes;
import eu.dzhw.fdz.metadatamanagement.domain.enumeration.ScaleLevel;

/**
 * A DTO for the Variable entity.
 */
public class VariableDTO implements Serializable {

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataTypes getDataType() {
        return dataType;
    }

    public void setDataType(DataTypes dataType) {
        this.dataType = dataType;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ScaleLevel getScaleLevel() {
        return scaleLevel;
    }

    public void setScaleLevel(ScaleLevel scaleLevel) {
        this.scaleLevel = scaleLevel;
    }

    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }

    public String getSurveyTitle() {
        return surveyTitle;
    }

    public void setSurveyTitle(String surveyTitle) {
        this.surveyTitle = surveyTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        VariableDTO variableDTO = (VariableDTO) o;

        if ( ! Objects.equals(id, variableDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "VariableDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", dataType='" + dataType + "'" +
            ", label='" + label + "'" +
            ", question='" + question + "'" +
            ", scaleLevel='" + scaleLevel + "'" +
            '}';
    }
}

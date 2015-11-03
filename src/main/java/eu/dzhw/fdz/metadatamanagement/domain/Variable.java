package eu.dzhw.fdz.metadatamanagement.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import eu.dzhw.fdz.metadatamanagement.domain.enumeration.DataTypes;

import eu.dzhw.fdz.metadatamanagement.domain.enumeration.ScaleLevel;

/**
 * A Variable.
 */
@Entity
@Table(name = "variable")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "variable")
public class Variable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 256)
    @Column(name = "name", length = 256, nullable = false)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "data_type", nullable = false)
    private DataTypes dataType;

    @Column(name = "label")
    private String label;

    @Size(max = 256)
    @Column(name = "question", length = 256)
    private String question;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "scale_level", nullable = false)
    private ScaleLevel scaleLevel;

    @ManyToOne
    private Survey survey;

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

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Variable variable = (Variable) o;

        if ( ! Objects.equals(id, variable.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Variable{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", dataType='" + dataType + "'" +
            ", label='" + label + "'" +
            ", question='" + question + "'" +
            ", scaleLevel='" + scaleLevel + "'" +
            '}';
    }
}

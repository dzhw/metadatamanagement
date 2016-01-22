package eu.dzhw.fdz.metadatamanagement.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.base.MoreObjects;

import eu.dzhw.fdz.metadatamanagement.domain.enumeration.DataType;
import eu.dzhw.fdz.metadatamanagement.domain.enumeration.ScaleLevel;
import eu.dzhw.fdz.metadatamanagement.domain.util.Patterns;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * A Variable.
 */
@Document(collection = "variables")
@GeneratePojoBuilder(intoPackage = "eu.dzhw.fdz.metadatamanagement.domain.builders")
@CompoundIndex(def = "{name: 1, fdz_project: 1}", unique = true)
public class Variable extends AbstractFdzDomainObject {
  @Id
  @NotEmpty
  private String id;

  @NotEmpty
  @Size(max = 32)
  @Pattern(regexp = Patterns.ALPHANUMERIC_WITH_UNDERSCORE)
  private String name;

  @NotNull
  private DataType dataType;

  @NotNull
  private ScaleLevel scaleLevel;

  @NotBlank
  @Size(max = 128)
  private String label;

  @DBRef(lazy = true)
  @NotNull
  private FdzProject fdzProject;
  
  @DBRef(lazy = true)
  private Survey survey;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public DataType getDataType() {
    return dataType;
  }

  public void setDataType(DataType dataType) {
    this.dataType = dataType;
  }

  public ScaleLevel getScaleLevel() {
    return scaleLevel;
  }

  public void setScaleLevel(ScaleLevel scaleLevel) {
    this.scaleLevel = scaleLevel;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public FdzProject getFdzProject() {
    return fdzProject;
  }

  public Survey getSurvey() {
    return survey;
  }

  public void setSurvey(Survey survey) {
    this.survey = survey;
  }

  public void setFdzProject(FdzProject fdzProject) {
    this.fdzProject = fdzProject;
  }
  
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .add("super", super.toString())
      .add("name", name)
      .add("dataType", dataType)
      .add("scaleLevel", scaleLevel)
      .add("label", label)
      .add("fdzProject", fdzProject)
      .add("survey", survey)
      .toString();
  }
}

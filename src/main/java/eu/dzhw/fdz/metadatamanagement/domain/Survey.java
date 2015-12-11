package eu.dzhw.fdz.metadatamanagement.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import eu.dzhw.fdz.metadatamanagement.domain.util.Patterns;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * A Survey.
 */

@Document(collection = "survey")
@GeneratePojoBuilder(intoPackage = "eu.dzhw.fdz.metadatamanagement.domain.builders")
public class Survey implements Serializable {

  private static final long serialVersionUID = -9080907927711709470L;

  @Id
  @NotBlank
  @Pattern(regexp = Patterns.ALPHANUMERIC_WITH_UNDERSCORE)
  private String id;

  @NotBlank
  @Field("title")
  private String title;

  @NotNull
  @Field("field_period")
  private LocalDate fieldPeriod;

  @NotBlank
  @Field("fdz_project_name")
  private String fdzProjectName;


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public LocalDate getFieldPeriod() {
    return fieldPeriod;
  }

  public void setFieldPeriod(LocalDate fieldPeriod) {
    this.fieldPeriod = fieldPeriod;
  }

  public String getFdzProjectName() {
    return fdzProjectName;
  }

  public void setFdzProjectName(String fdzProjectName) {
    this.fdzProjectName = fdzProjectName;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    Survey survey = (Survey) object;
    return Objects.equals(id, survey.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "Survey{" + "id=" + id + ", title='" + title + "'" + ", fieldPeriod='" + fieldPeriod
        + "'" + ", fdzProjectName='" + fdzProjectName + "'" + '}';
  }
}

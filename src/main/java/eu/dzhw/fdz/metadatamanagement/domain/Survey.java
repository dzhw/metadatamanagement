package eu.dzhw.fdz.metadatamanagement.domain;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.base.MoreObjects;

import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * A Survey.
 */

@Document(collection = "survey")
@GeneratePojoBuilder(intoPackage = "eu.dzhw.fdz.metadatamanagement.domain.builders")
public class Survey extends AbstractFdzDomainObject {

  @NotBlank
  @Indexed(unique = true)
  private String fdzId;

  @NotNull
  private I18nString title;

  @NotNull
  @Valid
  private Period fieldPeriod;

  @DBRef
  @NotNull
  private FdzProject fdzProject;

  public I18nString getTitle() {
    return title;
  }

  public void setTitle(I18nString title) {
    this.title = title;
  }

  public Period getFieldPeriod() {
    return fieldPeriod;
  }

  public void setFieldPeriod(Period fieldPeriod) {
    this.fieldPeriod = fieldPeriod;
  }

  public FdzProject getFdzProject() {
    return fdzProject;
  }

  public String getFdzId() {
    return fdzId;
  }

  public void setFdzId(String fdzId) {
    this.fdzId = fdzId;
  }

  public void setFdzProject(FdzProject fdzProject) {
    this.fdzProject = fdzProject;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .add("super", super.toString())
      .add("title", title)
      .add("fieldPeriod", fieldPeriod)
      .add("fdzProject", fdzProject)
      .toString();
  }
}

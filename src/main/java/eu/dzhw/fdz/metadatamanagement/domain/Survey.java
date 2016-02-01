package eu.dzhw.fdz.metadatamanagement.domain;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.base.MoreObjects;

import eu.dzhw.fdz.metadatamanagement.domain.validation.FdzProjectExists;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * A Survey.
 */
@Document(collection = "surveys")
@GeneratePojoBuilder(intoPackage = "eu.dzhw.fdz.metadatamanagement.domain.builders")
public class Survey extends AbstractFdzDomainObject {
  @Id
  @NotEmpty
  private String id;

  @NotNull
  private I18nString title;

  @NotNull
  @Valid
  private Period fieldPeriod;

  @NotEmpty
  @FdzProjectExists
  private String fdzProjectId;

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

  public String getFdzProjectId() {
    return fdzProjectId;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setFdzProjectId(String fdzProjectId) {
    this.fdzProjectId = fdzProjectId;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .add("super", super.toString())
      .add("title", title)
      .add("fieldPeriod", fieldPeriod)
      .add("fdzProjectId", fdzProjectId)
      .toString();
  }  
}

package eu.dzhw.fdz.metadatamanagement.domain;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.base.MoreObjects;

import eu.dzhw.fdz.metadatamanagement.domain.validation.DataAcquisitionProjectExists;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * A Survey.
 * 
 * @author Daniel Katzberg
 */
@Document(collection = "surveys")
@GeneratePojoBuilder(intoPackage = "eu.dzhw.fdz.metadatamanagement.domain.builders")
public class Survey extends AbstractRdcDomainObject {

  @Id
  @NotEmpty
  private String id;

  @NotNull
  private I18nString title;

  @NotNull
  @Valid
  private Period fieldPeriod;

  @NotEmpty
  @DataAcquisitionProjectExists
  private String dataAcquisitionProjectId;

  @NotEmpty
  private String questionnaireId;

  /*
   * (non-Javadoc)
   * 
   * @see eu.dzhw.fdz.metadatamanagement.domain.AbstractRdcDomainObject#getId()
   */
  @Override
  public String getId() {
    return id;
  }

  /*
   * (non-Javadoc)
   * 
   * @see eu.dzhw.fdz.metadatamanagement.domain.AbstractRdcDomainObject#toString()
   */
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .add("super", super.toString())
      .add("title", title)
      .add("fieldPeriod", fieldPeriod)
      .add("dataAcquisitionProjectId", dataAcquisitionProjectId)
      .toString();
  }

  /* GETTER / SETTER */
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

  public void setId(String id) {
    this.id = id;
  }

  public String getDataAcquisitionProjectId() {
    return dataAcquisitionProjectId;
  }

  public void setDataAcquisitionProjectId(String dataAcquisitionProjectId) {
    this.dataAcquisitionProjectId = dataAcquisitionProjectId;
  }

  public String getQuestionnaireId() {
    return questionnaireId;
  }

  public void setQuestionnaireId(String questionnaireId) {
    this.questionnaireId = questionnaireId;
  }
}

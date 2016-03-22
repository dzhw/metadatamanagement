package eu.dzhw.fdz.metadatamanagement.domain;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.base.MoreObjects;

import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * A Survey.
 * 
 * @author Daniel Katzberg
 */
@Document(collection = "surveys")
@GeneratePojoBuilder(intoPackage = "eu.dzhw.fdz.metadatamanagement.domain.builders")
public class Survey extends AbstractRdcDomainObject {

  /* Domain Object Attributes */
  @Id
  @NotEmpty(message = "{error.survey.id.isEmpty}")
  private String id;

  @NotNull(message = "{error.survey.title.isEmpty}")
  private I18nString title;

  @NotNull(message = "{error.survey.fieldPeriod.isEmpty}")
  @Valid
  private Period fieldPeriod;

  /* Foreign Keys */
  @NotEmpty(message = "{error.dataAcquisitionProject.id.isEmpty}")
  private String dataAcquisitionProjectId;

  @NotEmpty(message = "{error.questionnaire.id.isEmpty}")
  private String questionnaireId;
  
  private List<String> dataSetIds;

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

  public List<String> getDataSetIds() {
    return dataSetIds;
  }

  public void setDataSetIds(List<String> dataSetIds) {
    this.dataSetIds = dataSetIds;
  }
}

package eu.dzhw.fdz.metadatamanagement.domain;

import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.base.MoreObjects;

import eu.dzhw.fdz.metadatamanagement.domain.validation.DataAcquisitionProjectExists;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * Data Set.
 * 
 * @author Daniel Katzberg
 *
 */
@Document(collection = "data_sets")
@GeneratePojoBuilder(intoPackage = "eu.dzhw.fdz.metadatamanagement.domain.builders")
public class DataSet extends AbstractRdcDomainObject {
  @Id
  @NotEmpty
  private String id;

  private I18nString description;

  private List<String> variableIds;
  
  @NotEmpty
  @DataAcquisitionProjectExists
  private String dataAcquisitionProjectId;

  private List<String> surveyIds;

  /*
   * (non-Javadoc)
   * 
   * @see eu.dzhw.fdz.metadatamanagement.domain.AbstractRdcDomainObject#getId()
   */
  @Override
  public String getId() {
    return this.id;
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
      .add("id", id)
      .add("dataAcquisitionProjectId", dataAcquisitionProjectId)
      .add("description", description)
      .add("variableIds", variableIds)
      .add("surveyIds", surveyIds)
      .toString();
  }


  /* GETTER / SETTER */
  public I18nString getDescription() {
    return description;
  }

  public void setDescription(I18nString description) {
    this.description = description;
  }

  public List<String> getVariableIds() {
    return variableIds;
  }

  public void setVariableIds(List<String> variableIds) {
    this.variableIds = variableIds;
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


  public List<String> getSurveyIds() {
    return surveyIds;
  }


  public void setSurveyIds(List<String> surveyIds) {
    this.surveyIds = surveyIds;
  }
}

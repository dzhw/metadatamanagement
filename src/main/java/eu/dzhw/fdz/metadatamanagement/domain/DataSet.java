package eu.dzhw.fdz.metadatamanagement.domain;

import java.util.List;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.base.MoreObjects;

import eu.dzhw.fdz.metadatamanagement.domain.util.Patterns;
import eu.dzhw.fdz.metadatamanagement.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.domain.validation.ValidDataSetIdName;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * Data Set.
 * 
 * @author Daniel Katzberg
 *
 */
@Document(collection = "data_sets")
@GeneratePojoBuilder(intoPackage = "eu.dzhw.fdz.metadatamanagement.domain.builders")
@ValidDataSetIdName(message = "{error.dataSet.id.validDataSetIdName}")
public class DataSet extends AbstractRdcDomainObject {
  
  /* Domain Object Attributes */
  @Id
  @NotEmpty(message = "{error.dataSet.id.notEmpty}")
  @Size(max = StringLengths.MEDIUM, message = "{error.dataSet.id.size}")
  @Pattern(regexp = Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS,
      message = "{error.dataSet.id.pattern}")
  private String id;

  @I18nStringSize(max = StringLengths.LARGE, message = "{error.dataSet.description.18nStringSize}")
  private I18nString description;
  
  
  /* Foreign Keys */
  @NotEmpty(message = "{error.dataSet.dataAcquisitionProject.id.notEmpty}")
  private String dataAcquisitionProjectId;

  private List<String> surveyIds;
  
  private List<String> variableIds;

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

package eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.base.MoreObjects;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.util.Patterns;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.validation.ValidDataSetIdName;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.validation.ValidDataSetType;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * Data Set.
 * 
 * @author Daniel Katzberg
 *
 */
@Document(collection = "data_sets")
@GeneratePojoBuilder(
    intoPackage = "eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.builders")
@ValidDataSetIdName(message = "data-set-management.error.data-set.id.valid-data-set-id-name")
public class DataSet extends AbstractRdcDomainObject {
  
  /* Domain Object Attributes */
  @Id
  @NotEmpty(message = "data-set-management.error.data-set.id.not-empty")
  @Size(max = StringLengths.MEDIUM, message = "data-set-management.error.data-set.id.size")
  @Pattern(regexp = Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS,
      message = "data-set-management.error.data-set.id.pattern")
  private String id;

  @I18nStringSize(max = StringLengths.LARGE, 
      message = "data-set-management.error.data-set.description.i18n-string-size")
  private I18nString description;
  
  @NotNull(message = "data-set-management.error.data-set.type.not-null")
  @ValidDataSetType(message = "data-set-management.error.data-set.type.valid-type")
  private I18nString type;
  
  @NotEmpty(message = "data-set-management.error.data-set.survey-numbers.not-empty")
  private List<Integer> surveyNumbers;
  
  /* Foreign Keys */
  @Indexed
  @NotEmpty(message = "data-set-management.error.data-set.data-acquisition-project.id.not-empty")
  private String dataAcquisitionProjectId;

  @NotEmpty(message = "data-set-management.error.data-set.survey.ids.not-empty")
  private List<String> surveyIds;
  
  /* Nested Objects */
  @Valid
  @NotEmpty(message = "data-set-management.error.data-set.sub-data-sets.not-empty")
  private List<SubDataSet> subDataSets;

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
      .add("variableIds", surveyNumbers)
      .add("surveyIds", surveyIds)
      .add("type", type)
      .add("subDataSets", subDataSets)
      .toString();
  }


  /* GETTER / SETTER */
  public I18nString getDescription() {
    return description;
  }

  public void setDescription(I18nString description) {
    this.description = description;
  }
  
  public I18nString getType() {
    return type;
  }
  
  public void setType(I18nString type) {
    this.type = type;
  }

  public List<Integer> getSurveyNumbers() {
    return surveyNumbers;
  }

  public void setSurveyNumbers(List<Integer> surveyNumbers) {
    this.surveyNumbers = surveyNumbers;
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
  
  public List<SubDataSet> getSubDataSets() {
    return subDataSets;
  }

  public void setSubDataSets(List<SubDataSet> subDataSets) {
    this.subDataSets = subDataSets;
  } 
}

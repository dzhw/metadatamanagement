package eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.base.MoreObjects;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.validation.UniqueDatasetNumberInProject;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.validation.UniqueSubDatasetAccessWayInDataSet;
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
@UniqueDatasetNumberInProject(
    message = "data-set-management.error.data-set.unique-data-set-number-in-project")
@CompoundIndex(def = "{number: 1, dataAcquisitionProjectId: 1}", unique = true)
public class DataSet extends DataSetSubDocument {
  
  @NotNull(message = "data-set-management.error.data-set.type.not-null")
  @ValidDataSetType(message = "data-set-management.error.data-set.type.valid-type")
  private I18nString type;
  
  @NotEmpty(message = "data-set-management.error.data-set.survey-numbers.not-empty")
  private List<Integer> surveyNumbers;
  
  /* Foreign Keys */
  @Indexed
  @NotEmpty(message = "data-set-management.error.data-set.study.id.not-empty")
  private String studyId;

  @NotEmpty(message = "data-set-management.error.data-set.survey.ids.not-empty")
  private List<String> surveyIds;
  
  /* Nested Objects */
  @Valid
  @NotEmpty(message = "data-set-management.error.data-set.sub-data-sets.not-empty")
  @UniqueSubDatasetAccessWayInDataSet(message = "data-set-management.error.data-set."
          + "sub-data-sets.access-way-unique-within-data-set")
  private List<SubDataSet> subDataSets;

  public DataSet() {
    super();
  }
  
  public DataSet(DataSet dataSet) {
    super();
    BeanUtils.copyProperties(dataSet, this);
  }
  
  /*
   * (non-Javadoc)
   * @see eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject#toString()
   */
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .add("super", super.toString())
      .add("id", id)
      .add("description", description)
      .add("type", type)
      .add("surveyNumbers", surveyNumbers)
      .add("number", number)
      .add("format", format)
      .add("dataAcquisitionProjectId", dataAcquisitionProjectId)
      .add("studyId", studyId)
      .add("surveyIds", surveyIds)
      .add("subDataSets", subDataSets)
      .toString();
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

  public String getStudyId() {
    return studyId;
  }

  public void setStudyId(String studyId) {
    this.studyId = studyId;
  }
}

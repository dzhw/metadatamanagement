package eu.dzhw.fdz.metadatamanagement.domain;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.base.MoreObjects;

import eu.dzhw.fdz.metadatamanagement.domain.util.Patterns;
import eu.dzhw.fdz.metadatamanagement.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.domain.validation.UniqueVariableNameInProject;
import eu.dzhw.fdz.metadatamanagement.domain.validation.ValidAccessWays;
import eu.dzhw.fdz.metadatamanagement.domain.validation.ValidDataType;
import eu.dzhw.fdz.metadatamanagement.domain.validation.ValidScaleLevel;
import eu.dzhw.fdz.metadatamanagement.domain.validation.ValidVariableIdName;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * A Variable.
 * 
 * @author René Reitmann
 * @author Daniel Katzberg
 */
@Document(collection = "variables")
@GeneratePojoBuilder(intoPackage = "eu.dzhw.fdz.metadatamanagement.domain.builders")
@CompoundIndex(def = "{name: 1, dataAcquisitionProjectId: 1}", unique = true)
@ValidVariableIdName(message = "{error.variable.validVariableName}")
@UniqueVariableNameInProject(message = "{error.variable.uniqueVariableNameInProject}")
public class Variable extends AbstractRdcDomainObjectWithProjectSurvey {

  /* Domain Object listed attributes */
  @Id
  @NotEmpty(message = "{error.variable.id.notEmpty}")
  @Size(max = StringLengths.MEDIUM, message = "{error.variable.id.size}")
  @Pattern(regexp = Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS,
      message = "{error.variable.id.pattern}")
  private String id;

  @NotNull(message = "{error.variable.dataType.notNull}")
  @ValidDataType(message = "{error.variable.dataType.validDataType}")
  private I18nString dataType;

  @NotNull(message = "{error.variable.scaleLevel.notNull}")
  @ValidScaleLevel(message = "{error.variable.scaleLevel.validScaleLevel}")
  private I18nString scaleLevel;
  
  @NotEmpty(message = "{error.variable.name.notEmpty}")
  @Size(max = StringLengths.SMALL, message = "{error.variable.name.size}")
  @Pattern(regexp = Patterns.ALPHANUMERIC_WITH_UNDERSCORE_NO_NUMBER_AS_FIRST_SIGN,
      message = "{error.variable.name.pattern}")
  private String name;  

  @NotNull(message = "{error.variable.label.notNull}")
  @I18nStringSize(min = 1, max = StringLengths.MEDIUM,
      message = "{error.variable.label.i18nStringSize}")
  private I18nString label;
  
  @I18nStringSize(max = StringLengths.LARGE,
      message = "{error.variable.description.i18nStringSize}")
  private I18nString description;
  
  // checks for min size too.
  @ValidAccessWays(message = "{error.variable.accessWays.validAccessWays}")
  private List<String> accessWays;
  

  /* Nested Objects */
  @Valid
  private List<Value> values;
  
  private I18nSvg distributionSvg;
  
  @Valid
  private FilterDetails filterDetails;

  private List<String> sameVariablesInPanel;
  
  private Statistics statistics;

  private GenerationDetails generationDetails;
  

  /* Foreign Keys */
  private String conceptId;  

  private String atomicQuestionId;

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
   * @see eu.dzhw.fdz.metadatamanagement.domain.AbstractRdcDomainObjectWithProjectSurvey#toString()
   */
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .add("super", super.toString())
      .add("id", id)
      .add("name", name)
      .add("dataType", dataType)
      .add("scaleLevel", scaleLevel)
      .add("label", label)
      .add("values", values)
      .add("description", description)
      .add("accessWays", accessWays)
      .add("distributionSvg", distributionSvg)
      .add("filterDetails", filterDetails)
      .add("sameVariablesInPanel", sameVariablesInPanel)
      .add("conceptId", conceptId)
      .add("statistics", statistics)
      .add("generationDetails", generationDetails)
      .add("atomicQuestionId", atomicQuestionId)
      .add("dataSetIds", dataSetIds)
      .toString();
  }



  /* GETTER / SETTER */
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public I18nString getDataType() {
    return dataType;
  }

  public void setDataType(I18nString dataType) {
    this.dataType = dataType;
  }

  public I18nString getScaleLevel() {
    return scaleLevel;
  }

  public void setScaleLevel(I18nString scaleLevel) {
    this.scaleLevel = scaleLevel;
  }

  public I18nString getLabel() {
    return label;
  }

  public void setLabel(I18nString label) {
    this.label = label;
  }

  public I18nString getDescription() {
    return description;
  }

  public void setDescription(I18nString description) {
    this.description = description;
  }

  public List<String> getAccessWays() {
    return accessWays;
  }

  public void setAccessWays(List<String> accessWays) {
    this.accessWays = accessWays;
  }

  public I18nSvg getDistributionSvg() {
    return distributionSvg;
  }

  public void setDistributionSvg(I18nSvg distributionSvg) {
    this.distributionSvg = distributionSvg;
  }

  public void setId(String id) {
    this.id = id;
  }

  public List<Value> getValues() {
    return values;
  }

  public void setValues(List<Value> values) {
    this.values = values;
  }

  public List<String> getSameVariablesInPanel() {
    return sameVariablesInPanel;
  }

  public void setSameVariablesInPanel(List<String> sameVariablesInPanel) {
    this.sameVariablesInPanel = sameVariablesInPanel;
  }

  public String getConceptId() {
    return conceptId;
  }

  public void setConceptId(String conceptId) {
    this.conceptId = conceptId;
  }

  public Statistics getStatistics() {
    return statistics;
  }

  public void setStatistics(Statistics statistics) {
    this.statistics = statistics;
  }

  public GenerationDetails getGenerationDetails() {
    return generationDetails;
  }

  public void setGenerationDetails(GenerationDetails generationDetails) {
    this.generationDetails = generationDetails;
  }

  public String getAtomicQuestionId() {
    return atomicQuestionId;
  }

  public void setAtomicQuestionId(String atomicQuestionId) {
    this.atomicQuestionId = atomicQuestionId;
  }

  public List<String> getDataSetIds() {
    return dataSetIds;
  }

  public void setDataSetIds(List<String> dataSetIds) {
    this.dataSetIds = dataSetIds;
  }

  public FilterDetails getFilterDetails() {
    return filterDetails;
  }

  public void setFilterDetails(FilterDetails filterDetails) {
    this.filterDetails = filterDetails;
  }
}

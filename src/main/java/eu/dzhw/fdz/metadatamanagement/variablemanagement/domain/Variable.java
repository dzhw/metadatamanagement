package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.base.MoreObjects;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.util.Patterns;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.AtLeastOneLanguage;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.MandatoryScaleLevelForNumericDataType;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.UniqueVariableNameInProject;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.ValidAccessWays;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.ValidDataType;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.ValidResponseValueMustBeANumberOnNumericDataType;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.ValidResponseValueMustBeAnIsoDateOnDateDataType;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.ValidScaleLevel;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.ValidVariableIdName;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * A Variable.
 *
 * @author René Reitmann
 * @author Daniel Katzberg
 */
@Document(collection = "variables")
@GeneratePojoBuilder(
    intoPackage = "eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.builders")
@CompoundIndex(def = "{name: 1, dataAcquisitionProjectId: 1}", unique = true)
@ValidVariableIdName(message = "variable.error.variable.validVariableName")
@UniqueVariableNameInProject(message = "variable.error."
    + "variable.uniqueVariableNameInProject")
@MandatoryScaleLevelForNumericDataType(
    message = "variable.error.variable.mandatoryScaleLevelForNumericDataType")
@ValidResponseValueMustBeANumberOnNumericDataType(
    message = "variable.error.variable."
        + "validResponseValueMustBeANumberOnNumericDataType")
@ValidResponseValueMustBeAnIsoDateOnDateDataType(
    message = "variable.error.variable."
        + "validResponseValueMustBeAnIsoDateOnDateDataType")
public class Variable extends AbstractRdcDomainObject {

  /* Domain Object listed attributes */
  @Id
  @NotEmpty(message = "variable.error.variable.id.notEmpty")
  @Size(max = StringLengths.MEDIUM,
      message = "variable.error.variable.id.size")
  @Pattern(regexp = Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS,
      message = "variable.error.variable.id.pattern")
  private String id;

  @NotNull(message = "variable.error.variable.dataType.notNull")
  @ValidDataType(message = "variable.error.variable.dataType.validDataType")
  private I18nString dataType;

  @ValidScaleLevel(
      message = "variable.error.variable.scaleLevel.validScaleLevel")
  private I18nString scaleLevel;

  @NotEmpty(message = "variable.error.variable.name.notEmpty")
  @Size(max = StringLengths.SMALL,
      message = "variable.error.variable.name.size")
  @Pattern(regexp = Patterns.ALPHANUMERIC_WITH_UNDERSCORE_NO_NUMBER_AS_FIRST_SIGN,
      message = "variable.error.variable.name.pattern")
  private String name;

  @NotNull(message = "variable.error.variable.label.notNull")
  @I18nStringSize(max = StringLengths.MEDIUM,
      message = "variable.error.variable.label.i18nStringSize")
  @AtLeastOneLanguage(
      message = "variable.error.variable.label.atLeastOneLanguage")
  private I18nString label;

  @I18nStringSize(max = StringLengths.LARGE,
      message = "variable.error.variable.description.i18nStringSize")
  private I18nString description;

  // checks for min size too.
  @NotEmpty(message = "variable.error.variable.accessWays.notEmpty")
  @ValidAccessWays(
      message = "variable.error.variable.accessWays.validAccessWays")
  private List<String> accessWays;

  private List<String> sameVariablesInPanel;

  /* Nested Objects */
  @Valid
  private FilterDetails filterDetails;

  @Valid
  private GenerationDetails generationDetails;

  @Valid
  private Distribution distribution;


  /* Foreign Keys */
  private String atomicQuestionId;

  @NotEmpty(message = "variable.error.variable.dataSet.ids.notEmpty")
  private List<String> dataSetIds;

  @Indexed
  private String dataAcquisitionProjectId;

  @NotEmpty(message = "variable.error.variable.survey.ids.notEmpty")
  private List<String> surveyIds;

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
   * @see eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject#toString()
   */
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .add("super", super.toString())
      .add("id", id)
      .add("dataType", dataType)
      .add("scaleLevel", scaleLevel)
      .add("name", name)
      .add("label", label)
      .add("description", description)
      .add("accessWays", accessWays)
      .add("sameVariablesInPanel", sameVariablesInPanel)
      .add("filterDetails", filterDetails)
      .add("generationDetails", generationDetails)
      .add("distribution", distribution)
      .add("atomicQuestionId", atomicQuestionId)
      .add("dataSetIds", dataSetIds)
      .add("dataAcquisitionProjectId", dataAcquisitionProjectId)
      .add("surveyIds", surveyIds)
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

  public void setId(String id) {
    this.id = id;
  }

  public List<String> getSameVariablesInPanel() {
    return sameVariablesInPanel;
  }

  public void setSameVariablesInPanel(List<String> sameVariablesInPanel) {
    this.sameVariablesInPanel = sameVariablesInPanel;
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

  public Distribution getDistribution() {
    return distribution;
  }

  public void setDistribution(Distribution distribution) {
    this.distribution = distribution;
  }
}

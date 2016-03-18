package eu.dzhw.fdz.metadatamanagement.domain;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.base.MoreObjects;

import eu.dzhw.fdz.metadatamanagement.domain.util.Patterns;
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
public class Variable extends AbstractRdcDomainObjectWithProjectSurvey {

  @Id
  @NotEmpty(message = "{error.variable.id.isEmpty}")
  private String id;

  @NotEmpty(message = "{error.variable.name.isEmpty}")
  @Size(max = 32)
  @Pattern(regexp = Patterns.ALPHANUMERIC_WITH_UNDERSCORE)
  private String name;

  @NotNull(message = "{error.variable.name.isEmpty}")
  private I18nString dataType;

  @NotNull(message = "{error.variable.scaleLevel.isEmpty}")
  private I18nString scaleLevel;

  @NotNull(message = "{error.variable.label.isEmpty}")
  private I18nString label;

  private List<Value> values;

  private I18nString description;

  private List<String> accessWays;

  private String filterExpression;

  private I18nString filterDescription;

  private String filterExpressionLanguage;

  private I18nSvg distributionSvg;

  private List<String> sameVariablesInPanel;

  private String conceptId;

  private Statistics statistics;

  private GenerationDetails generationDetails;

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
   * 
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
      .add("filterExpression", filterExpression)
      .add("filterDescription", filterDescription)
      .add("filterExpressionLanguage", filterExpressionLanguage)
      .add("distributionSvg", distributionSvg)
      .add("sameVariablesInPanel", sameVariablesInPanel)
      .add("conceptId", conceptId)
      .add("statistics", statistics)
      .add("generationDetails", generationDetails)
      .add("atomicQuestionId", atomicQuestionId)
      .add("dataSetsIds", dataSetIds)
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

  public String getFilterExpression() {
    return filterExpression;
  }

  public void setFilterExpression(String filterExpression) {
    this.filterExpression = filterExpression;
  }

  public I18nString getFilterDescription() {
    return filterDescription;
  }

  public void setFilterDescription(I18nString filterDescription) {
    this.filterDescription = filterDescription;
  }

  public String getFilterExpressionLanguage() {
    return filterExpressionLanguage;
  }

  public void setFilterExpressionLanguage(String filterExpressionLanguage) {
    this.filterExpressionLanguage = filterExpressionLanguage;
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
}

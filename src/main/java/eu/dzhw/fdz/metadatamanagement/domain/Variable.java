package eu.dzhw.fdz.metadatamanagement.domain;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.base.MoreObjects;

import eu.dzhw.fdz.metadatamanagement.domain.enumeration.AccessWay;
import eu.dzhw.fdz.metadatamanagement.domain.enumeration.DataType;
import eu.dzhw.fdz.metadatamanagement.domain.enumeration.FilterExpressionLanguage;
import eu.dzhw.fdz.metadatamanagement.domain.enumeration.ScaleLevel;
import eu.dzhw.fdz.metadatamanagement.domain.util.Patterns;
import eu.dzhw.fdz.metadatamanagement.domain.validation.SurveyHasSameDataAcquisitionProject;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * A Variable.
 * 
 * @author René Reitmann
 * @author Daniel Katzberg
 */
@Document(collection = "variables")
@GeneratePojoBuilder(intoPackage = "eu.dzhw.fdz.metadatamanagement.domain.builders")
@CompoundIndex(def = "{name: 1, fdz_project: 1}", unique = true)
@SurveyHasSameDataAcquisitionProject
public class Variable extends AbstractRdcDomainObjectWithProjectSurvey {
  @Id
  @NotEmpty
  private String id;

  @NotEmpty
  @Size(max = 32)
  @Pattern(regexp = Patterns.ALPHANUMERIC_WITH_UNDERSCORE)
  private String name;

  @NotNull
  private DataType dataType;

  @NotNull
  private ScaleLevel scaleLevel;

  @NotBlank
  @Size(max = 128)
  private String label;

  private List<Value> values;

  private I18nString description;

  private List<AccessWay> accessWays;

  private String filterExpression;

  private I18nString filterDescription;

  private FilterExpressionLanguage filterExpressionLanguage;

  private I18nSvg distributionSvg;

  private List<Variable> sameVariablesInPanel;

  private String conceptId;

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
      .toString();
  }



  /* GETTER / SETTER */
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public DataType getDataType() {
    return dataType;
  }

  public void setDataType(DataType dataType) {
    this.dataType = dataType;
  }

  public ScaleLevel getScaleLevel() {
    return scaleLevel;
  }

  public void setScaleLevel(ScaleLevel scaleLevel) {
    this.scaleLevel = scaleLevel;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public I18nString getDescription() {
    return description;
  }

  public void setDescription(I18nString description) {
    this.description = description;
  }

  public List<AccessWay> getAccessWays() {
    return accessWays;
  }

  public void setAccessWays(List<AccessWay> accessWays) {
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

  public FilterExpressionLanguage getFilterExpressionLanguage() {
    return filterExpressionLanguage;
  }

  public void setFilterExpressionLanguage(FilterExpressionLanguage filterExpressionLanguage) {
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

  public List<Variable> getSameVariablesInPanel() {
    return sameVariablesInPanel;
  }

  public void setSameVariablesInPanel(List<Variable> sameVariablesInPanel) {
    this.sameVariablesInPanel = sameVariablesInPanel;
  }

  public String getConceptId() {
    return conceptId;
  }

  public void setConceptId(String conceptId) {
    this.conceptId = conceptId;
  }
}

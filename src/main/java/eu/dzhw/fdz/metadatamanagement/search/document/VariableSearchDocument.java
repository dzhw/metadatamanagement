package eu.dzhw.fdz.metadatamanagement.search.document;

import eu.dzhw.fdz.metadatamanagement.domain.Variable;
import io.searchbox.annotations.JestId;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * Document which is stored in elasticsearch in order to be able to search for variables.
 * 
 * @author René Reitmann
 */
@GeneratePojoBuilder(intoPackage = "eu.dzhw.fdz.metadatamanagement.search.document.builders")
public class VariableSearchDocument {

  @JestId
  private String id;

  private String dataType;

  private String scaleLevel;

  private String label;

  private String name;
  
  private String fdzProjectName;
  
  private String surveyId;

  public VariableSearchDocument() {}

  /**
   * Generate a search document from the given entity.
   * 
   * @param variable a variable entity from mongo
   */
  public VariableSearchDocument(Variable variable) {
    this.id = variable.getId();
    // TODO map to localized string
    this.dataType = variable.getDataType().toString();
    // TODO map to localized string
    this.scaleLevel = variable.getScaleLevel().toString();
    this.label = variable.getLabel();
    this.name = variable.getName();
    this.surveyId = variable.getSurveyId();
    this.fdzProjectName = variable.getFdzProjectName();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getDataType() {
    return dataType;
  }

  public void setDataType(String dataType) {
    this.dataType = dataType;
  }

  public String getScaleLevel() {
    return scaleLevel;
  }

  public void setScaleLevel(String scaleLevel) {
    this.scaleLevel = scaleLevel;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getFdzProjectName() {
    return fdzProjectName;
  }

  public void setFdzProjectName(String fdzProjectName) {
    this.fdzProjectName = fdzProjectName;
  }

  public String getSurveyId() {
    return surveyId;
  }

  public void setSurveyId(String surveyId) {
    this.surveyId = surveyId;
  }
}

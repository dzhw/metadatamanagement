package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.util.ArrayList;
import java.util.List;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchIndices;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import io.searchbox.annotations.JestId;

/**
 * Representation of a variable which is stored in elasticsearch.
 *
 * @author René Reitmann
 * @author Daniel Katzberg
 */
public class VariableSearchDocument {
  @JestId
  private String id;

  private String name;

  private String dataAcquisitionProjectId;

  private String label;

  private String dataType;

  private String scaleLevel;
  
  private String relatedQuestionStrings;
  
  private String annotations;

  private List<String> surveyTitles;
  
  private List<String> dataSetIds;

  /**
   * Create the search document from the domain object depending on the language (index).
   */
  public VariableSearchDocument(Variable variable, Iterable<Survey> surveys,
      Iterable<DataSet> dataSets, ElasticsearchIndices index) {
    this.id = variable.getId();
    this.name = variable.getName();
    this.dataAcquisitionProjectId = variable.getDataAcquisitionProjectId();
    createI18nAttributes(variable, index);
    createSurveyTitles(surveys, index);
    createDataSetIds(dataSets);
  }

  private void createSurveyTitles(Iterable<Survey> surveys, ElasticsearchIndices index) {
    if (surveys != null) {
      surveyTitles = new ArrayList<>();
      for (Survey survey : surveys) {
        switch (index) {
          case METADATA_DE:
            surveyTitles.add(survey.getTitle()
                .getDe());            
            break;
          case METADATA_EN:
            surveyTitles.add(survey.getTitle()
                .getEn());
            break;
          default:
            throw new RuntimeException("Unknown index:" + index);
        }
      }
    }
  }
  
  private void createDataSetIds(Iterable<DataSet> dataSets) {
    if (dataSets != null) {
      dataSetIds = new ArrayList<>();
      for (DataSet dataSet : dataSets) {
        dataSetIds.add(dataSet.getId());
      }
    }
  }
  
  private void createI18nAttributes(Variable variable, ElasticsearchIndices index) {
    switch (index) {
      case METADATA_DE:
        label = variable.getLabel() != null ? variable.getLabel().getDe() : null;
        dataType = variable.getDataType() != null ? variable.getDataType().getDe() : null;
        scaleLevel = variable.getScaleLevel() != null ? variable.getScaleLevel().getDe() : null;
        setRelatedQuestionStrings(variable.getRelatedQuestionStrings() != null 
            ? variable.getRelatedQuestionStrings().getDe() : null);
        annotations = variable.getAnnotations() != null ? variable.getAnnotations().getDe() : null;
        break;
      case METADATA_EN:
        label = variable.getLabel() != null ? variable.getLabel().getEn() : null;
        dataType = variable.getDataType() != null ? variable.getDataType().getEn() : null;
        scaleLevel = variable.getScaleLevel() != null ? variable.getScaleLevel().getEn() : null;
        setRelatedQuestionStrings(variable.getRelatedQuestionStrings() != null 
            ? variable.getRelatedQuestionStrings().getEn() : null);
        annotations = variable.getAnnotations() != null ? variable.getAnnotations().getEn() : null;
        break;
      default:
        throw new RuntimeException("Unknown index:" + index);
    }
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDataAcquisitionProjectId() {
    return dataAcquisitionProjectId;
  }

  public void setDataAcquisitionProjectId(String dataAcquisitionProjectId) {
    this.dataAcquisitionProjectId = dataAcquisitionProjectId;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
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

  public List<String> getSurveyTitles() {
    return surveyTitles;
  }

  public void setSurveyTitles(List<String> surveyTitles) {
    this.surveyTitles = surveyTitles;
  }

  public List<String> getDataSetIds() {
    return dataSetIds;
  }

  public void setDataSetIds(List<String> dataSetIds) {
    this.dataSetIds = dataSetIds;
  }

  public String getRelatedQuestionStrings() {
    return relatedQuestionStrings;
  }

  public void setRelatedQuestionStrings(String relatedQuestionStrings) {
    this.relatedQuestionStrings = relatedQuestionStrings;
  }

  public String getAnnotations() {
    return annotations;
  }

  public void setAnnotations(String annotations) {
    this.annotations = annotations;
  }
}

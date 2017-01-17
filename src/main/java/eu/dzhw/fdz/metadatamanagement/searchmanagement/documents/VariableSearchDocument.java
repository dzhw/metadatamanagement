package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.util.ArrayList;
import java.util.List;

import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchIndices;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.RelatedQuestion;
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
  
  private String dataSetId;
  
  private Integer dataSetNumber;
  
  private Integer indexInDataSet;
  
  private List<Integer> surveyNumbers;
  
  private List<RelatedQuestion> relatedQuestions;

  /**
   * Create the search document from the domain object depending on the language (index).
   */
  public VariableSearchDocument(Variable variable, Iterable<Survey> surveys,
      ElasticsearchIndices index) {
    this.id = variable.getId();
    this.name = variable.getName();
    this.dataAcquisitionProjectId = variable.getDataAcquisitionProjectId();
    this.dataSetId = variable.getDataSetId();
    this.dataSetNumber = variable.getDataSetNumber();
    this.indexInDataSet = variable.getIndexInDataSet();
    this.surveyNumbers = variable.getSurveyNumbers();
    this.relatedQuestions = variable.getRelatedQuestions();
    createI18nAttributes(variable, index);
    createSurveyTitles(surveys, index);
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
  
  public List<RelatedQuestion> getRelatedQuestions() {
    return relatedQuestions;
  }

  public void setRelatedQuestions(List<RelatedQuestion> relatedQuestions) {
    this.relatedQuestions = relatedQuestions;
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

  public Integer getDataSetNumber() {
    return dataSetNumber;
  }

  public void setDataSetNumber(Integer dataSetNumber) {
    this.dataSetNumber = dataSetNumber;
  }

  public List<String> getSurveyTitles() {
    return surveyTitles;
  }

  public void setSurveyTitles(List<String> surveyTitles) {
    this.surveyTitles = surveyTitles;
  }
  
  public List<Integer> getSurveyNumbers() {
    return surveyNumbers;
  }

  public void setSurveyNumbers(List<Integer> surveyNumbers) {
    this.surveyNumbers = surveyNumbers;
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

  public String getDataSetId() {
    return dataSetId;
  }

  public void setDataSetId(String dataSetId) {
    this.dataSetId = dataSetId;
  }

  public Integer getIndexInDataSet() {
    return indexInDataSet;
  }

  public void setIndexInDataSet(Integer indexInDataSet) {
    this.indexInDataSet = indexInDataSet;
  }


}

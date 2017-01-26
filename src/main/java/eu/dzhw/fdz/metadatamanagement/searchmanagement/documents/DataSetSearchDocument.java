package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.util.ArrayList;
import java.util.List;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.SubDataSet;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchIndices;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import io.searchbox.annotations.JestId;

/**
 * Representation of a dataSet which is stored in elasticsearch.
 *
 * @author René Reitmann
 * @author Daniel Katzberg
 */
public class DataSetSearchDocument {
  @JestId
  private String id;
  
  private String dataAcquisitionProjectId;

  private String description;
  
  private String type;
  
  private List<String> surveyTitles;
  
  private List<String> surveyIds;
  
  private List<Integer> surveyNumbers;
  
  private Integer number;
  
  private List<SubDataSetSearchDocument> subDataSets;
  
  private String format;
 

  /**
   * Create the search document from the domain object depending on the language (index).
   */
  public DataSetSearchDocument(DataSet dataSet, Iterable<Survey> surveys,
      ElasticsearchIndices index) {
    this.id = dataSet.getId();
    this.dataAcquisitionProjectId = dataSet.getDataAcquisitionProjectId();
    this.setSurveyIds(dataSet.getSurveyIds());
    this.surveyNumbers = dataSet.getSurveyNumbers();
    this.number = dataSet.getNumber();
    createSubDataSetAttributes(dataSet, index);
    createI18nAttributes(dataSet, index);
    createSurveyTitles(surveys, index);
  }

  private void createSubDataSetAttributes(DataSet dataSet, ElasticsearchIndices index) {
    subDataSets = new ArrayList<>();
    if (dataSet.getSubDataSets() == null) {
      return; 
    }
    
    for (SubDataSet subDataSet : dataSet.getSubDataSets()) {
      subDataSets.add(new SubDataSetSearchDocument(subDataSet, index));
    }
  }
  
  private void createI18nAttributes(DataSet dataSet, ElasticsearchIndices index) {
    switch (index) {
      case METADATA_DE:
        description = dataSet.getDescription() != null ? dataSet.getDescription().getDe() : null;
        type = dataSet.getType() != null ? dataSet.getType().getDe() : null;
        format = dataSet.getFormat() != null ? dataSet.getFormat().getDe() : null;
        break;
      case METADATA_EN:
        description = dataSet.getDescription() != null ? dataSet.getDescription().getEn() : null;
        type = dataSet.getType() != null ? dataSet.getType().getEn() : null;
        format = dataSet.getFormat() != null ? dataSet.getFormat().getEn() : null;
        break;
      default:
        throw new RuntimeException("Unknown index:" + index);
    }
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
  
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public List<String> getSurveyTitles() {
    return surveyTitles;
  }

  public void setSurveyTitles(List<String> surveyTitles) {
    this.surveyTitles = surveyTitles;
  }

  public List<String> getSurveyIds() {
    return surveyIds;
  }

  public void setSurveyIds(List<String> surveyIds) {
    this.surveyIds = surveyIds;
  }

  public String getDataAcquisitionProjectId() {
    return dataAcquisitionProjectId;
  }

  public void setDataAcquisitionProjectId(String dataAcquisitionProjectId) {
    this.dataAcquisitionProjectId = dataAcquisitionProjectId;
  }

  public List<SubDataSetSearchDocument> getSubDataSets() {
    return subDataSets;
  }

  public void setSubDataSets(List<SubDataSetSearchDocument> subDataSets) {
    this.subDataSets = subDataSets;
  }

  public List<Integer> getSurveyNumbers() {
    return surveyNumbers;
  }

  public void setSurveyNumbers(List<Integer> surveyNumbers) {
    this.surveyNumbers = surveyNumbers;
  }

  public Integer getNumber() {
    return number;
  }

  public void setNumber(Integer number) {
    this.number = number;
  }

  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }
}

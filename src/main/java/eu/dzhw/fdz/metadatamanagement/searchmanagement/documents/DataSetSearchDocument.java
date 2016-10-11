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
 */
public class DataSetSearchDocument {
  @JestId
  private String id;
  
  private String dataAcquisitionProjectId;

  private String description;
  
  private String type;
  
  private List<String> surveyTitles;
  
  private List<String> variableIds;
  
  private List<SubDataSetSearchDocument> subDataSets;
 

  /**
   * Create the search document from the domain object depending on the language (index).
   */
  public DataSetSearchDocument(DataSet dataSet, Iterable<Survey> surveys,
      ElasticsearchIndices index) {
    this.id = dataSet.getId();
    this.dataAcquisitionProjectId = dataSet.getDataAcquisitionProjectId();
    this.variableIds = dataSet.getVariableIds();
    createSubDataSetAttributes(dataSet, index);
    createI18nAttributes(dataSet, index);
    createSurveyTitles(surveys, index);
  }

  private void createSubDataSetAttributes(DataSet dataSet, ElasticsearchIndices index) {
    subDataSets = new ArrayList<SubDataSetSearchDocument>();
    
    for (SubDataSet subDataSet : dataSet.getSubDataSets()) {
      subDataSets.add(new SubDataSetSearchDocument(subDataSet, index));
    }
  }
  
  private void createI18nAttributes(DataSet dataSet, ElasticsearchIndices index) {
    switch (index) {
      case METADATA_DE:
        description = dataSet.getDescription() != null ? dataSet.getDescription().getDe() : null;
        type = dataSet.getType() != null ? dataSet.getDescription().getDe() : null;
        break;
      case METADATA_EN:
        description = dataSet.getDescription() != null ? dataSet.getDescription().getEn() : null;
        type = dataSet.getType() != null ? dataSet.getDescription().getEn() : null;
        break;
      default:
        throw new RuntimeException("Unknown index:" + index);
    }
  }
  
  private void createSurveyTitles(Iterable<Survey> surveys, ElasticsearchIndices index) {
    if (surveys != null) {
      surveyTitles = new ArrayList<String>();
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

  public String getDataAcquisitionProjectId() {
    return dataAcquisitionProjectId;
  }

  public void setDataAcquisitionProjectId(String dataAcquisitionProjectId) {
    this.dataAcquisitionProjectId = dataAcquisitionProjectId;
  }

  public List<String> getVariableIds() {
    return variableIds;
  }

  public void setVariableIds(List<String> variableIds) {
    this.variableIds = variableIds;
  }

  public List<SubDataSetSearchDocument> getSubDataSets() {
    return subDataSets;
  }

  public void setSubDataSets(List<SubDataSetSearchDocument> subDataSets) {
    this.subDataSets = subDataSets;
  }
}

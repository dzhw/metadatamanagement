package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.util.ArrayList;
import java.util.List;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
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

  private List<String> surveyTitles;

  /**
   * Create the search document from the domain object depending on the language (index).
   */
  public DataSetSearchDocument(DataSet dataSet, Iterable<Survey> surveys,
      ElasticsearchIndices index) {
    this.id = dataSet.getId();
    this.dataAcquisitionProjectId = dataSet.getDataAcquisitionProjectId();
    createI18nAttributes(dataSet, index);
    createSurveyTitles(surveys, index);
  }

  private void createI18nAttributes(DataSet dataSet, ElasticsearchIndices index) {
    switch (index) {
      case METADATA_DE:
        description = dataSet.getDescription() != null ? dataSet.getDescription().getDe() : null;
        break;
      case METADATA_EN:
        description = dataSet.getDescription() != null ? dataSet.getDescription().getEn() : null;
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
}

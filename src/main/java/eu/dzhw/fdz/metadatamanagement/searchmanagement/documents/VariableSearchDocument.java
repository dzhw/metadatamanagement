package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.util.ArrayList;
import java.util.List;

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

  private List<String> surveyTitles;

  /**
   * Create the search document from the domain object depending on the language (index).
   */
  public VariableSearchDocument(Variable variable, Iterable<Survey> surveys,
      ElasticsearchIndices index) {
    this.id = variable.getId();
    this.name = variable.getName();
    this.dataAcquisitionProjectId = variable.getDataAcquisitionProjectId();
    createLabel(variable, index);
    createScaleLevel(variable, index);
    createDataType(variable, index);
    createSurveyTitles(surveys, index);
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

  private void createLabel(Variable variable, ElasticsearchIndices index) {
    switch (index) {
      case METADATA_DE:
        label = variable.getLabel()
          .getDe();
        break;
      case METADATA_EN:
        label = variable.getLabel()
          .getEn();
        break;
      default:
        throw new RuntimeException("Unknown index:" + index);
    }
  }



  private void createDataType(Variable variable, ElasticsearchIndices index) {
    switch (index) {
      case METADATA_DE: {
        this.dataType = variable.getDataType()
          .getDe();
        break;
      }
      case METADATA_EN:
        this.dataType = variable.getDataType()
          .getEn();
        break;
      default:
        throw new RuntimeException("Unknown index:" + index);
    }
  }


  private void createScaleLevel(Variable variable, ElasticsearchIndices index) {
    switch (index) {
      case METADATA_DE: {
        this.scaleLevel = variable.getScaleLevel()
          .getDe();
        break;
      }
      case METADATA_EN:
        this.scaleLevel = variable.getScaleLevel()
          .getEn();
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
}

package eu.dzhw.fdz.metadatamanagement.variablemanagement.search.document;

import eu.dzhw.fdz.metadatamanagement.common.service.enums.ElasticsearchIndices;
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

  private String surveyTitle;

  /**
   * Create the search document from the domain object depending on the language (index).
   */
  public VariableSearchDocument(Variable variable, Survey survey, ElasticsearchIndices index) {
    this.id = variable.getId();
    this.name = variable.getName();
    this.dataAcquisitionProjectId = variable.getDataAcquisitionProjectId();
    createLabel(variable, index);
    createScaleLevel(variable, index);
    createDataType(variable, index);
    createSurveyTitle(survey, index);
  }

  private void createSurveyTitle(Survey survey, ElasticsearchIndices index) {
    if (survey != null) {
      switch (index) {
        case METADATA_DE:
          surveyTitle = survey.getTitle()
            .getDe();
          break;
        case METADATA_EN:
          surveyTitle = survey.getTitle()
            .getEn();
          break;
        default:
          throw new RuntimeException("Unknown index:" + index);
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

  public String getSurveyTitle() {
    return surveyTitle;
  }

  public void setSurveyTitle(String surveyTitle) {
    this.surveyTitle = surveyTitle;
  }
}

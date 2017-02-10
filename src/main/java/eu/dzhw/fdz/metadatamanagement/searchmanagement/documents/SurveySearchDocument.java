package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.util.List;
import java.util.stream.Collectors;

import eu.dzhw.fdz.metadatamanagement.common.domain.Period;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchIndices;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import io.searchbox.annotations.JestId;

/**
 * Representation of a survey which is stored in elasticsearch.
 *
 * @author René Reitmann
 * @author Daniel Katzberg
 */
public class SurveySearchDocument {
  @JestId
  private String id;
  
  private String dataAcquisitionProjectId;

  private String title;

  private Period fieldPeriod;
  
  private String population;
  
  private String sample;
  
  private String surveyMethod;
  
  private Integer number;
  
  private String studyId;
  
  private List<String> instrumentIds;
  
  private List<String> variableIds;
  
  private List<String> dataSetIds;

  /**
   * Create the search document from the domain object depending on the language (index).
   */
  public SurveySearchDocument(Survey survey, List<Instrument> instruments,
      List<Variable> variables, List<DataSet> dataSets,ElasticsearchIndices index) {
    this.id = survey.getId();
    this.dataAcquisitionProjectId = survey.getDataAcquisitionProjectId();
    this.number = survey.getNumber();
    this.studyId = survey.getStudyId();
    createI18nAttributes(survey, index);
    this.fieldPeriod = survey.getFieldPeriod();
    if (instruments != null) {
      this.instrumentIds = instruments.stream()
          .map(Instrument::getId).collect(Collectors.toList());      
    }
    if (variables != null) {
      this.variableIds = variables.stream()
          .map(Variable::getId).collect(Collectors.toList());    
    }
    if (dataSets != null) {
      this.setDataSetIds(dataSets.stream()
          .map(DataSet::getId).collect(Collectors.toList()));    
    }
  }

  private void createI18nAttributes(Survey survey, ElasticsearchIndices index) {
    switch (index) {
      case METADATA_DE:
        title = survey.getTitle() != null ? survey.getTitle().getDe() : null;
        population = survey.getPopulation() != null ? survey.getPopulation().getDe() : null;
        sample = survey.getSample() != null ? survey.getSample().getDe() : null;
        surveyMethod = survey.getSurveyMethod() != null ? survey.getSurveyMethod().getDe() : null;
        break;
      case METADATA_EN:
        title = survey.getTitle() != null ? survey.getTitle().getEn() : null;
        population = survey.getPopulation() != null ? survey.getPopulation().getEn() : null;
        sample = survey.getSample() != null ? survey.getSample().getEn() : null;
        surveyMethod = survey.getSurveyMethod() != null ? survey.getSurveyMethod().getEn() : null;
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

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Period getFieldPeriod() {
    return fieldPeriod;
  }

  public void setFieldPeriod(Period fieldPeriod) {
    this.fieldPeriod = fieldPeriod;
  }
  
  public String getDataAcquisitionProjectId() {
    return dataAcquisitionProjectId;
  }

  public void setDataAcquisitionProjectId(String dataAcquisitionProjectId) {
    this.dataAcquisitionProjectId = dataAcquisitionProjectId;
  }

  public String getPopulation() {
    return population;
  }

  public void setPopulation(String population) {
    this.population = population;
  }

  public String getSample() {
    return sample;
  }

  public void setSample(String sample) {
    this.sample = sample;
  }

  public String getSurveyMethod() {
    return surveyMethod;
  }

  public void setSurveyMethod(String surveyMethod) {
    this.surveyMethod = surveyMethod;
  }

  public Integer getNumber() {
    return number;
  }

  public void setNumber(Integer number) {
    this.number = number;
  }
  
  public List<String> getInstrumentIds() {
    return instrumentIds;
  }

  public void setInstrumentIds(List<String> instrumentIds) {
    this.instrumentIds = instrumentIds;
  }

  public List<String> getVariableIds() {
    return variableIds;
  }

  public void setVariableIds(List<String> variableIds) {
    this.variableIds = variableIds;
  }

  public List<String> getDataSetIds() {
    return dataSetIds;
  }

  public void setDataSetIds(List<String> dataSetIds) {
    this.dataSetIds = dataSetIds;
  }

  public String getStudyId() {
    return studyId;
  }

  public void setStudyId(String studyId) {
    this.studyId = studyId;
  }
}

package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.projections.DataSetSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.projections.InstrumentSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.projections.QuestionSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.projections.RelatedPublicationSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.SurveyDataTypes;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.projections.SurveySubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.projections.VariableSubDocumentProjection;

/**
 * Representation of a study which is stored in elasticsearch.
 *
 * @author René Reitmann
 */
public class StudySearchDocument extends Study implements SearchDocumentInterface {
  private List<DataSetSubDocument> dataSets = 
      new ArrayList<>();
  
  private List<VariableSubDocument> variables = 
      new ArrayList<>();
  
  private List<RelatedPublicationSubDocument> relatedPublications = 
      new ArrayList<>();
  
  private List<SurveySubDocument> surveys = 
      new ArrayList<>();
  
  private List<QuestionSubDocument> questions = 
      new ArrayList<>();
  
  private List<InstrumentSubDocument> instruments = 
      new ArrayList<>();
      
  private Release release = null;
  
  private I18nString surveyDataType;
  
  private Integer numberOfWaves;
  
  private I18nString guiLabels = StudyDetailsGuiLabels.GUI_LABELS;
  
  /**
   * Construct the search document with all related subdocuments.
   * @param study The study to be searched for
   * @param dataSets all data sets available in this study
   * @param variables all variables available in this study
   * @param relatedPublications all related publications using this study
   * @param surveys all surveys available in this study
   * @param questions all questions available in this study
   * @param instruments all instruments available in this study
   */
  @SuppressWarnings("CPD-START")
  public StudySearchDocument(Study study,
      List<DataSetSubDocumentProjection> dataSets,
      List<VariableSubDocumentProjection> variables, 
      List<RelatedPublicationSubDocumentProjection> relatedPublications,
      List<SurveySubDocumentProjection> surveys,
      List<QuestionSubDocumentProjection> questions, 
      List<InstrumentSubDocumentProjection> instruments,
      Release release) {
    super(study);
    if (dataSets != null) {
      this.dataSets = dataSets.stream()
          .map(DataSetSubDocument::new).collect(Collectors.toList());      
    }
    if (variables != null) {
      this.variables = variables.stream()
          .map(VariableSubDocument::new).collect(Collectors.toList());      
    }
    if (relatedPublications != null) {
      this.relatedPublications = relatedPublications.stream()
          .map(RelatedPublicationSubDocument::new).collect(Collectors.toList());
    }
    if (surveys != null) {
      this.surveys = surveys.stream()
          .map(SurveySubDocument::new).collect(Collectors.toList());      
      this.surveyDataType = generateSurveyDataType(surveys);
      this.numberOfWaves = generateNumberOfWaves(surveys);
    }
    if (questions != null) {
      this.questions = questions.stream()
          .map(QuestionSubDocument::new).collect(Collectors.toList());      
    }
    if (instruments != null) {
      this.instruments = instruments.stream()
          .map(InstrumentSubDocument::new).collect(Collectors.toList());      
    }
    this.release = release;
  }
  
  /**
   * Check the wave number of every survey. 
   * @param surveys All Survey Sub Document Projections.
   * @return The highest (max) wave number.
   */
  private Integer generateNumberOfWaves(List<SurveySubDocumentProjection> surveys) {
    Integer numberOfWaves = null;
    
    for (SurveySubDocumentProjection survey : surveys) {
      if (numberOfWaves == null || survey.getWave() > numberOfWaves) {
        numberOfWaves = survey.getWave();
      }
    }
    
    return numberOfWaves;
  }

  /**
   * Check the Data Type of every Survey.
   * @param surveys All Survey Sub Document Projections.
   * @return If all Data Type of the Survey are equal, return the Data Type. If the surveys have 
   *     different Data Type, return the Mixed Method Data Type. 
   */
  private I18nString generateSurveyDataType(List<SurveySubDocumentProjection> surveys) {
    
    I18nString surveyDataType = null;
    
    for (SurveySubDocumentProjection survey : surveys) {
      if (surveyDataType == null) {
        surveyDataType = survey.getDataType();
        continue;
      }
      
      if (survey.getDataType() != null && !surveyDataType.equals(survey.getDataType())) {
        return SurveyDataTypes.MIXED_METHODS;
      }
    }
    
    return surveyDataType;
  }

  public List<DataSetSubDocument> getDataSets() {
    return dataSets;
  }

  public void setDataSets(List<DataSetSubDocument> dataSets) {
    this.dataSets = dataSets;
  }

  public List<VariableSubDocument> getVariables() {
    return variables;
  }

  public void setVariables(List<VariableSubDocument> variables) {
    this.variables = variables;
  }

  public List<RelatedPublicationSubDocument> getRelatedPublications() {
    return relatedPublications;
  }

  public void setRelatedPublications(List<RelatedPublicationSubDocument> relatedPublications) {
    this.relatedPublications = relatedPublications;
  }

  public List<SurveySubDocument> getSurveys() {
    return surveys;
  }

  public void setSurveys(List<SurveySubDocument> surveys) {
    this.surveys = surveys;
  }

  public List<QuestionSubDocument> getQuestions() {
    return questions;
  }

  public void setQuestions(List<QuestionSubDocument> questions) {
    this.questions = questions;
  }

  public List<InstrumentSubDocument> getInstruments() {
    return instruments;
  }

  public void setInstruments(List<InstrumentSubDocument> instruments) {
    this.instruments = instruments;
  }

  public Release getRelease() {
    return release;
  }

  public void setRelease(Release release) {
    this.release = release;
  }

  public I18nString getSurveyDataType() {
    return surveyDataType;
  }

  public void setSurveyDataType(I18nString surveyDataType) {
    this.surveyDataType = surveyDataType;
  }

  public Integer getNumberOfWaves() {
    return numberOfWaves;
  }
  
  public void setNumberOfWaves(Integer numberOfWaves) {
    this.numberOfWaves = numberOfWaves;
  }
  
  @Override
  public I18nString getGuiLabels() {
    return guiLabels;
  }

  public void setGuiLabels(I18nString guiLabels) {
    this.guiLabels = guiLabels;
  }
}

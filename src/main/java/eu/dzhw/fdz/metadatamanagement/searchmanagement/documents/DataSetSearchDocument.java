package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.projections.InstrumentSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.projections.QuestionSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.projections.RelatedPublicationSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.projection.StudySubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.projections.SurveySubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.projections.VariableSubDocumentProjection;

/**
 * Representation of a dataSet which is stored in elasticsearch.
 *
 * @author René Reitmann
 */
public class DataSetSearchDocument extends DataSet {
  
  private StudySubDocument study = null;
  private List<VariableSubDocument> variables = 
      new ArrayList<VariableSubDocument>();
  private List<InstrumentSubDocument> instruments = 
      new ArrayList<InstrumentSubDocument>();
  private List<QuestionSubDocument> questions = 
      new ArrayList<QuestionSubDocument>();
  private List<RelatedPublicationSubDocument> relatedPublications = 
      new ArrayList<RelatedPublicationSubDocument>();
  private List<SurveySubDocument> surveys = 
      new ArrayList<SurveySubDocument>();
  private Release release = null;
  
  private Integer maxNumberOfObservations;
  
  private List<String> accessWays;
  
  /**
   * Construct the search document with all related subdocuments.
   * @param dataSet The data set to be searched for.
   * @param study The study containing this data set.
   * @param variables The variables available in this data set.
   * @param relatedPublications The related publications using this data set.
   * @param surveys The surveys using this data set.
   * @param instruments The instruments used to create this data set.
   * @param questions The questions used to create this data set.
   */
  @SuppressWarnings("CPD-START")
  public DataSetSearchDocument(DataSet dataSet, 
      StudySubDocumentProjection study, 
      List<VariableSubDocumentProjection> variables,
      List<RelatedPublicationSubDocumentProjection> relatedPublications, 
      List<SurveySubDocumentProjection> surveys,
      List<InstrumentSubDocumentProjection> instruments,
      List<QuestionSubDocumentProjection> questions,
      Release release) {
    super(dataSet);
    if (study != null) {
      this.study = new StudySubDocument(study);            
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
    }
    if (instruments != null) {
      this.instruments = instruments.stream()
          .map(InstrumentSubDocument::new).collect(Collectors.toList());      
    }
    if (questions != null) {
      this.questions = questions.stream()
          .map(QuestionSubDocument::new).collect(Collectors.toList());      
    }
    this.maxNumberOfObservations = dataSet.getSubDataSets().stream()
        .map(subDataSet -> subDataSet.getNumberOfObservations()).reduce(Integer::max).get();
    this.accessWays = dataSet.getSubDataSets().stream()
        .map(subDataSet -> subDataSet.getAccessWay()).collect(Collectors.toList());
    this.release = release;
  }

  public StudySubDocument getStudy() {
    return study;
  }

  public void setStudy(StudySubDocument study) {
    this.study = study;
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
  
  public List<InstrumentSubDocument> getInstruments() {
    return instruments;
  }

  public void setInstruments(List<InstrumentSubDocument> instruments) {
    this.instruments = instruments;
  }
  
  public List<QuestionSubDocument> getQuestion() {
    return questions;
  }

  public void setQuestions(List<QuestionSubDocument> questions) {
    this.questions = questions;
  }

  public Release getRelease() {
    return release;
  }

  public void setRelease(Release release) {
    this.release = release;
  }

  public Integer getMaxNumberOfObservations() {
    return maxNumberOfObservations;
  }

  public List<String> getAccessWays() {
    return accessWays;
  }

  public List<QuestionSubDocument> getQuestions() {
    return questions;
  }
}

package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.util.ArrayList;
import java.util.List;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.projections.DataSetSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.projections.InstrumentSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.projections.QuestionSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.projections.RelatedPublicationSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.projections.SurveySubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.projections.VariableSubDocumentProjection;

/**
 * Representation of a study which is stored in elasticsearch.
 *
 * @author René Reitmann
 */
public class StudySearchDocument extends Study {
  private List<DataSetSubDocumentProjection> dataSets = 
      new ArrayList<DataSetSubDocumentProjection>();
  
  private List<VariableSubDocumentProjection> variables = 
      new ArrayList<VariableSubDocumentProjection>();
  
  private List<RelatedPublicationSubDocumentProjection> relatedPublications = 
      new ArrayList<RelatedPublicationSubDocumentProjection>();
  
  private List<SurveySubDocumentProjection> surveys = 
      new ArrayList<SurveySubDocumentProjection>();
  
  private List<QuestionSubDocumentProjection> questions = 
      new ArrayList<QuestionSubDocumentProjection>();
  
  private List<InstrumentSubDocumentProjection> instruments = 
      new ArrayList<InstrumentSubDocumentProjection>();
  
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
      List<InstrumentSubDocumentProjection> instruments) {
    super(study);
    if (dataSets != null) {
      this.dataSets = dataSets;      
    }
    if (variables != null) {
      this.variables = variables;      
    }
    if (relatedPublications != null) {
      this.relatedPublications = relatedPublications;
    }
    if (surveys != null) {
      this.surveys = surveys;
    }
    if (questions != null) {
      this.questions = questions;      
    }
    if (instruments != null) {
      this.instruments = instruments;      
    }
  }

  public List<DataSetSubDocumentProjection> getDataSets() {
    return dataSets;
  }


  public List<VariableSubDocumentProjection> getVariables() {
    return variables;
  }

  public List<RelatedPublicationSubDocumentProjection> getRelatedPublications() {
    return relatedPublications;
  }

  public List<SurveySubDocumentProjection> getSurveys() {
    return surveys;
  }

  public List<QuestionSubDocumentProjection> getQuestions() {
    return questions;
  }

  public List<InstrumentSubDocumentProjection> getInstruments() {
    return instruments;
  }
}

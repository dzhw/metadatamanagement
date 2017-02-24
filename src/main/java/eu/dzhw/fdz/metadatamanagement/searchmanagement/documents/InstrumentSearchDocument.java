package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.util.ArrayList;
import java.util.List;

import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.projections.QuestionSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.projections.RelatedPublicationSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.StudySubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.projections.SurveySubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.projections.VariableSubDocumentProjection;

/**
 * Representation of an instrument which is stored in elasticsearch.
 *
 * @author René Reitmann
 */
public class InstrumentSearchDocument extends Instrument {
  private StudySubDocumentProjection study;
  private List<SurveySubDocumentProjection> surveys = 
      new ArrayList<SurveySubDocumentProjection>();
  private List<QuestionSubDocumentProjection> questions = 
      new ArrayList<QuestionSubDocumentProjection>();
  private List<VariableSubDocumentProjection> variables = 
      new ArrayList<VariableSubDocumentProjection>();
  private List<RelatedPublicationSubDocumentProjection> relatedPublications = 
      new ArrayList<RelatedPublicationSubDocumentProjection>();
  
  /**
   * Construct the search document with all related subdocuments.
   * @param instrument the instrument to be searched for
   * @param study the study containing this instrument
   * @param surveys the surveys using this intrument
   * @param questions the questions used by this instrument
   * @param variables the variables used by the questions of this instrument
   * @param relatedPublications the related publications using this instrument
   */
  @SuppressWarnings("CPD-START")
  public InstrumentSearchDocument(Instrument instrument, 
      StudySubDocumentProjection study, 
      List<SurveySubDocumentProjection> surveys,
      List<QuestionSubDocumentProjection> questions, 
      List<VariableSubDocumentProjection> variables,
      List<RelatedPublicationSubDocumentProjection> relatedPublications) {
    super(instrument);
    this.study = study;
    if (surveys != null) {
      this.surveys = surveys; 
    }
    if (questions != null) {
      this.questions = questions;      
    }
    if (variables != null) {
      this.variables = variables;      
    }
    if (relatedPublications != null) {
      this.relatedPublications = relatedPublications;
    }
  }

  public StudySubDocumentProjection getStudy() {
    return study;
  }

  public List<SurveySubDocumentProjection> getSurveys() {
    return surveys;
  }

  public List<QuestionSubDocumentProjection> getQuestions() {
    return questions;
  }

  public List<VariableSubDocumentProjection> getVariables() {
    return variables;
  }


  public List<RelatedPublicationSubDocumentProjection> getRelatedPublications() {
    return relatedPublications;
  } 
}

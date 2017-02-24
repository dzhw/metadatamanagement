package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.util.ArrayList;
import java.util.List;

import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.projections.InstrumentSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.projections.RelatedPublicationSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.StudySubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.projections.SurveySubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.projections.VariableSubDocumentProjection;

/**
 * Representation of an question which is stored in elasticsearch.
 */
public class QuestionSearchDocument extends Question {
  private StudySubDocumentProjection study = null;
  private InstrumentSubDocumentProjection instrument = null;
  private List<SurveySubDocumentProjection> surveys = 
      new ArrayList<SurveySubDocumentProjection>();
  private List<VariableSubDocumentProjection> variables = 
      new ArrayList<VariableSubDocumentProjection>();
  private List<RelatedPublicationSubDocumentProjection> relatedPublications = 
      new ArrayList<RelatedPublicationSubDocumentProjection>();
  
  /**
   * Construct the search document with all related subdocuments.
   * @param question the question to be searched for
   * @param study the study containing this question
   * @param instrument the instrument containing this question
   * @param surveys the surveys using this question
   * @param variables the variables used by this question
   * @param relatedPublications all publication using this question
   */
  @SuppressWarnings("CPD-START")
  public QuestionSearchDocument(Question question, 
      StudySubDocumentProjection study, 
      InstrumentSubDocumentProjection instrument,
      List<SurveySubDocumentProjection> surveys, List<VariableSubDocumentProjection> variables, 
      List<RelatedPublicationSubDocumentProjection> relatedPublications) {
    super(question);
    this.study = study;      
    if (instrument != null) {
      this.instrument = instrument;      
    }
    if (surveys != null) {
      this.surveys = surveys;      
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

  public InstrumentSubDocumentProjection getInstrument() {
    return instrument;
  }

  public List<SurveySubDocumentProjection> getSurveys() {
    return surveys;
  }

  public List<VariableSubDocumentProjection> getVariables() {
    return variables;
  }

  public List<RelatedPublicationSubDocumentProjection> getRelatedPublications() {
    return relatedPublications;
  }
}

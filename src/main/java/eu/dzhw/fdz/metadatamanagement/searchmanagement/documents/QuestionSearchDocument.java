package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.projections.InstrumentSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.projections.RelatedPublicationSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.projection.StudySubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.projections.SurveySubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.projections.VariableSubDocumentProjection;

/**
 * Representation of an question which is stored in elasticsearch.
 */
public class QuestionSearchDocument extends Question {
  private StudySubDocument study = null;
  private InstrumentSubDocument instrument = null;
  private List<SurveySubDocument> surveys = 
      new ArrayList<SurveySubDocument>();
  private List<VariableSubDocument> variables = 
      new ArrayList<VariableSubDocument>();
  private List<RelatedPublicationSubDocument> relatedPublications = 
      new ArrayList<RelatedPublicationSubDocument>();
  
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
    if (study != null) {
      this.study = new StudySubDocument(study);            
    }
    if (instrument != null) {
      this.instrument = new InstrumentSubDocument(instrument);      
    }
    if (surveys != null) {
      this.surveys = surveys.stream()
          .map(SurveySubDocument::new).collect(Collectors.toList());      
    }
    if (variables != null) {
      this.variables = variables.stream()
          .map(VariableSubDocument::new).collect(Collectors.toList());
    }
    if (relatedPublications != null) {
      this.relatedPublications = relatedPublications.stream()
          .map(RelatedPublicationSubDocument::new).collect(Collectors.toList());      
    }
  }

  public StudySubDocument getStudy() {
    return study;
  }

  public void setStudy(StudySubDocument study) {
    this.study = study;
  }

  public InstrumentSubDocument getInstrument() {
    return instrument;
  }

  public void setInstrument(InstrumentSubDocument instrument) {
    this.instrument = instrument;
  }

  public List<SurveySubDocument> getSurveys() {
    return surveys;
  }

  public void setSurveys(List<SurveySubDocument> surveys) {
    this.surveys = surveys;
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
}

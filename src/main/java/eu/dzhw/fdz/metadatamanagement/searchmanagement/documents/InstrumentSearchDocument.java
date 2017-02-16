package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.QuestionSubDocument;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublicationSubDocument;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.StudySubDocument;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.SurveySubDocument;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.VariableSubDocument;

/**
 * Representation of an instrument which is stored in elasticsearch.
 *
 * @author René Reitmann
 */
public class InstrumentSearchDocument extends Instrument {
  private StudySubDocument study;
  private List<SurveySubDocument> surveys = new ArrayList<SurveySubDocument>();
  private List<QuestionSubDocument> questions = new ArrayList<QuestionSubDocument>();
  private List<VariableSubDocument> variables = new ArrayList<VariableSubDocument>();
  private List<RelatedPublicationSubDocument> relatedPublications = 
      new ArrayList<RelatedPublicationSubDocument>();
  
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
  public InstrumentSearchDocument(Instrument instrument, Study study, 
      List<Survey> surveys, List<Question> questions, List<Variable> variables,
      List<RelatedPublication> relatedPublications) {
    super(instrument);
    if (study != null) {      
      this.study = new StudySubDocument(study);
    }
    if (surveys != null) {
      this.surveys = surveys.stream()
          .map(SurveySubDocument::new).collect(Collectors.toList());      
    }
    if (questions != null) {
      this.questions = questions.stream()
          .map(QuestionSubDocument::new).collect(Collectors.toList());      
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

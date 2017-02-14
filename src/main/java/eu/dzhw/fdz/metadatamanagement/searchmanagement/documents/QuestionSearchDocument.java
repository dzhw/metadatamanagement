package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.util.List;
import java.util.stream.Collectors;

import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.InstrumentSubDocument;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublicationSubDocument;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.StudySubDocument;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.SurveySubDocument;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.VariableSubDocument;

/**
 * Representation of an question which is stored in elasticsearch.
 */
public class QuestionSearchDocument extends Question {
  private StudySubDocument study;
  private InstrumentSubDocument instrument;
  private List<SurveySubDocument> surveys;
  private List<VariableSubDocument> variables;
  private List<RelatedPublicationSubDocument> relatedPublications;
  
  /**
   * Construct the search document with all related subdocuments.
   * @param question the question to be searched for
   * @param study the study containing this question
   * @param instrument the instrument containing this question
   * @param surveys the surveys using this question
   * @param variables the variables used by this question
   * @param relatedPublications all publication using this question
   */
  public QuestionSearchDocument(Question question, Study study, Instrument instrument,
      List<Survey> surveys, List<Variable> variables, 
      List<RelatedPublication> relatedPublications) {
    super(question);
    if (study != null) {
      this.study = new StudySubDocument(study);      
    }
    if (instrument != null) {
      this.instrument = new InstrumentSubDocument(instrument);      
    }
    this.surveys = surveys.stream().map(SurveySubDocument::new).collect(Collectors.toList());
    this.variables = variables.stream().map(VariableSubDocument::new).collect(Collectors.toList());
    this.relatedPublications = relatedPublications.stream()
        .map(RelatedPublicationSubDocument::new).collect(Collectors.toList());
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

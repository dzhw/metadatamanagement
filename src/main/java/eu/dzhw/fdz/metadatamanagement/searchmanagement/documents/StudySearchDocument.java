package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.util.List;
import java.util.stream.Collectors;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSetSubDocument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.InstrumentSubDocument;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.QuestionSubDocument;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublicationSubDocument;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.SurveySubDocument;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.VariableSubDocument;

/**
 * Representation of a study which is stored in elasticsearch.
 *
 * @author René Reitmann
 */
public class StudySearchDocument extends Study {
  private List<DataSetSubDocument> dataSets;
  
  private List<VariableSubDocument> variables;
  
  private List<RelatedPublicationSubDocument> relatedPublications;
  
  private List<SurveySubDocument> surveys;
  
  private List<QuestionSubDocument> questions;
  
  private List<InstrumentSubDocument> instruments;
  
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
  public StudySearchDocument(Study study, List<DataSet> dataSets,
      List<Variable> variables, List<RelatedPublication> relatedPublications,
      List<Survey> surveys, List<Question> questions, List<Instrument> instruments) {
    super(study);
    this.dataSets = dataSets.stream().map(DataSetSubDocument::new).collect(Collectors.toList());
    this.variables = variables.stream().map(VariableSubDocument::new).collect(Collectors.toList());
    this.relatedPublications = relatedPublications.stream()
        .map(RelatedPublicationSubDocument::new).collect(Collectors.toList());
    this.surveys = surveys.stream().map(SurveySubDocument::new).collect(Collectors.toList());
    this.questions = questions.stream().map(QuestionSubDocument::new).collect(Collectors.toList());
    this.instruments = instruments.stream()
        .map(InstrumentSubDocument::new).collect(Collectors.toList());
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
}

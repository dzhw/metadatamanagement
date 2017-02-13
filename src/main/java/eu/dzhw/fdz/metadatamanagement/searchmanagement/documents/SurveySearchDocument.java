package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.util.ArrayList;
import java.util.List;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSetSubDocument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.InstrumentSubDocument;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.QuestionSubDocument;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublicationSubDocument;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.StudySubDocument;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.VariableSubDocument;

/**
 * Representation of a survey which is stored in elasticsearch.
 *
 * @author René Reitmann
 * @author Daniel Katzberg
 */
public class SurveySearchDocument extends Survey {
  private StudySubDocument study;
  private List<DataSetSubDocument> dataSets;
  private List<VariableSubDocument> variables;
  private List<RelatedPublicationSubDocument> relatedPublications;
  private List<InstrumentSubDocument> instruments;
  private List<QuestionSubDocument> questions;
  
  /**
   * Construct the search document with all related subdocuments.
   * @param survey the survey to be searched for
   * @param study the study containing this survey
   * @param dataSets the data sets available for this survey
   * @param variables the variables available for this survey
   * @param relatedPublications the publication using this survey
   * @param instruments the instruments used by this survey
   * @param questions the questions used by this survey
   */
  public SurveySearchDocument(Survey survey, Study study, List<DataSet> dataSets, 
      List<Variable> variables, List<RelatedPublication> relatedPublications,
      List<Instrument> instruments, List<Question> questions) {
    super(survey);
    this.study = study;
    this.dataSets = new ArrayList<DataSetSubDocument>(dataSets);
    this.variables = new ArrayList<VariableSubDocument>(variables);
    this.relatedPublications = new ArrayList<RelatedPublicationSubDocument>(relatedPublications);
    this.instruments = new ArrayList<InstrumentSubDocument>(instruments);
    this.questions = new ArrayList<QuestionSubDocument>(questions);
  }

  public StudySubDocument getStudy() {
    return study;
  }

  public void setStudy(StudySubDocument study) {
    this.study = study;
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

  public List<InstrumentSubDocument> getInstruments() {
    return instruments;
  }

  public void setInstruments(List<InstrumentSubDocument> instruments) {
    this.instruments = instruments;
  }

  public List<QuestionSubDocument> getQuestions() {
    return questions;
  }

  public void setQuestions(List<QuestionSubDocument> questions) {
    this.questions = questions;
  }
}

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
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.StudySubDocument;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.SurveySubDocument;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.VariableSubDocument;

/**
 * Representation of a related publication which is stored in elasticsearch.
 *
 * @author Daniel Katzberg
 */
public class RelatedPublicationSearchDocument extends RelatedPublication {
  private List<StudySubDocument> studies;
  private List<QuestionSubDocument> questions;
  private List<InstrumentSubDocument> instruments;
  private List<SurveySubDocument> surveys;
  private List<DataSetSubDocument> dataSets;
  private List<VariableSubDocument> variables;
  
  /**
   * Construct the search document with all related subdocuments.
   * @param relatedPublication the related publication to be searched for
   * @param studies the studies for which the publication was published
   * @param questions the questions for which the publication was published
   * @param instruments the instruments for which the publication was published
   * @param surveys the surveys for which the publication was published
   * @param dataSets the dataSets for which the publication was published
   * @param variables the variables for which the publication was published
   */
  public RelatedPublicationSearchDocument(RelatedPublication relatedPublication,
      List<Study> studies, List<Question> questions, List<Instrument> instruments,
      List<Survey> surveys, List<DataSet> dataSets, List<Variable> variables) {
    super(relatedPublication);
    this.studies = studies.stream().map(StudySubDocument::new).collect(Collectors.toList());
    this.questions = questions.stream().map(QuestionSubDocument::new).collect(Collectors.toList());
    this.instruments = instruments.stream()
        .map(InstrumentSubDocument::new).collect(Collectors.toList());
    this.surveys = surveys.stream().map(SurveySubDocument::new).collect(Collectors.toList());
    this.dataSets = dataSets.stream().map(DataSetSubDocument::new).collect(Collectors.toList());
    this.variables = variables.stream().map(VariableSubDocument::new).collect(Collectors.toList());
  }

  public List<StudySubDocument> getStudies() {
    return studies;
  }

  public void setStudies(List<StudySubDocument> studies) {
    this.studies = studies;
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

  public List<SurveySubDocument> getSurveys() {
    return surveys;
  }

  public void setSurveys(List<SurveySubDocument> surveys) {
    this.surveys = surveys;
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
}

package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.util.ArrayList;
import java.util.List;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.projections.DataSetSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.projections.InstrumentSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.projections.QuestionSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.StudySubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.projections.SurveySubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.projections.VariableSubDocumentProjection;

/**
 * Representation of a related publication which is stored in elasticsearch.
 *
 * @author Daniel Katzberg
 */
public class RelatedPublicationSearchDocument extends RelatedPublication {
  private List<StudySubDocumentProjection> studies = 
      new ArrayList<StudySubDocumentProjection>();
  private List<QuestionSubDocumentProjection> questions = 
      new ArrayList<QuestionSubDocumentProjection>();
  private List<InstrumentSubDocumentProjection> instruments = 
      new ArrayList<InstrumentSubDocumentProjection>();
  private List<SurveySubDocumentProjection> surveys = 
      new ArrayList<SurveySubDocumentProjection>();
  private List<DataSetSubDocumentProjection> dataSets = 
      new ArrayList<DataSetSubDocumentProjection>();
  private List<VariableSubDocumentProjection> variables = 
      new ArrayList<VariableSubDocumentProjection>();
  
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
  @SuppressWarnings("CPD-START")
  public RelatedPublicationSearchDocument(RelatedPublication relatedPublication,
      List<StudySubDocumentProjection> studies, 
      List<QuestionSubDocumentProjection> questions, 
      List<InstrumentSubDocumentProjection> instruments,
      List<SurveySubDocumentProjection> surveys, 
      List<DataSetSubDocumentProjection> dataSets, 
      List<VariableSubDocumentProjection> variables) {
    super(relatedPublication);
    if (studies != null) {
      this.studies = studies;      
    }
    if (questions != null) {
      this.questions = questions;      
    }
    if (instruments != null) {
      this.instruments = instruments;      
    }
    if (surveys != null) {
      this.surveys = surveys;      
    }
    if (dataSets != null) {
      this.dataSets = dataSets;      
    }
    if (variables != null) {
      this.variables = variables;      
    }
  }

  public List<StudySubDocumentProjection> getStudies() {
    return studies;
  }

  public List<QuestionSubDocumentProjection> getQuestions() {
    return questions;
  }

  public List<InstrumentSubDocumentProjection> getInstruments() {
    return instruments;
  }

  public List<SurveySubDocumentProjection> getSurveys() {
    return surveys;
  }

  public List<DataSetSubDocumentProjection> getDataSets() {
    return dataSets;
  }
  
  public List<VariableSubDocumentProjection> getVariables() {
    return variables;
  }
}

package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.util.ArrayList;
import java.util.List;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.projections.DataSetSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.projections.InstrumentSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.projections.QuestionSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.projections.RelatedPublicationSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.StudySubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.projections.VariableSubDocumentProjection;

/**
 * Representation of a survey which is stored in elasticsearch.
 *
 * @author René Reitmann
 * @author Daniel Katzberg
 */
public class SurveySearchDocument extends Survey {
  private StudySubDocumentProjection study;
  private List<DataSetSubDocumentProjection> dataSets = 
      new ArrayList<DataSetSubDocumentProjection>();
  private List<VariableSubDocumentProjection> variables =
      new ArrayList<VariableSubDocumentProjection>();
  private List<RelatedPublicationSubDocumentProjection> relatedPublications = 
      new ArrayList<RelatedPublicationSubDocumentProjection>();
  private List<InstrumentSubDocumentProjection> instruments = 
      new ArrayList<InstrumentSubDocumentProjection>();
  private List<QuestionSubDocumentProjection> questions = 
      new ArrayList<QuestionSubDocumentProjection>();
  
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
  @SuppressWarnings("CPD-START")
  public SurveySearchDocument(Survey survey,
      StudySubDocumentProjection study, 
      List<DataSetSubDocumentProjection> dataSets, 
      List<VariableSubDocumentProjection> variables, 
      List<RelatedPublicationSubDocumentProjection> relatedPublications,
      List<InstrumentSubDocumentProjection> instruments,
      List<QuestionSubDocumentProjection> questions) {
    super(survey);
    this.study = study;      
    if (dataSets != null) {
      this.dataSets = dataSets;      
    }
    if (variables != null) {
      this.variables = variables;      
    }
    if (relatedPublications != null) {
      this.relatedPublications = relatedPublications;      
    }
    if (instruments != null) {
      this.instruments = instruments;      
    }
    if (questions != null) {
      this.questions = questions;      
    }
  }

  public StudySubDocumentProjection getStudy() {
    return study;
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

  public List<InstrumentSubDocumentProjection> getInstruments() {
    return instruments;
  }

  public List<QuestionSubDocumentProjection> getQuestions() {
    return questions;
  }
}

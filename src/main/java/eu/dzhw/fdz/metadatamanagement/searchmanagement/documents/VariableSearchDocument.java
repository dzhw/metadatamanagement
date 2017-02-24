package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.util.ArrayList;
import java.util.List;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.projections.DataSetSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.projections.InstrumentSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.projections.RelatedPublicationSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.StudySubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.projections.SurveySubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;

/**
 * Representation of a variable which is stored in elasticsearch.
 *
 * @author René Reitmann
 * @author Daniel Katzberg
 */
public class VariableSearchDocument extends Variable {
  private DataSetSubDocumentProjection dataSet;
  private StudySubDocumentProjection study;
  private List<RelatedPublicationSubDocumentProjection> relatedPublications = 
      new ArrayList<RelatedPublicationSubDocumentProjection>();
  private List<SurveySubDocumentProjection> surveys = 
      new ArrayList<SurveySubDocumentProjection>();
  private List<InstrumentSubDocumentProjection> instruments = 
      new ArrayList<InstrumentSubDocumentProjection>(); 
  
  /**
   * Construct the search document with all related subdocuments.
   * @param variable the variable to be searched for
   * @param dataSet the data set containing this variable
   * @param study the study containing this variable
   * @param relatedPublications the related publications using this variable
   * @param surveys the surveys using this variable
   * @param instruments the instruments using this variable
   */
  @SuppressWarnings("CPD-START")
  public VariableSearchDocument(Variable variable, 
      DataSetSubDocumentProjection dataSet, 
      StudySubDocumentProjection study,
      List<RelatedPublicationSubDocumentProjection> relatedPublications, 
      List<SurveySubDocumentProjection> surveys, 
      List<InstrumentSubDocumentProjection> instruments) {
    super(variable);
    this.dataSet = dataSet;
    this.study = study;      
    if (relatedPublications != null) {
      this.relatedPublications = relatedPublications;
    }
    if (surveys != null) {
      this.surveys = surveys;      
    }
    if (instruments != null) {
      this.instruments = instruments;
    }
  }

  public DataSetSubDocumentProjection getDataSet() {
    return dataSet;
  }

  public StudySubDocumentProjection getStudy() {
    return study;
  }

  public List<RelatedPublicationSubDocumentProjection> getRelatedPublications() {
    return relatedPublications;
  }

  public List<SurveySubDocumentProjection> getSurveys() {
    return surveys;
  }

  public List<InstrumentSubDocumentProjection> getInstruments() {
    return instruments;
  }
}

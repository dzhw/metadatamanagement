package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.util.ArrayList;
import java.util.List;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.projections.RelatedPublicationSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.StudySubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.projections.SurveySubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.projections.VariableSubDocumentProjection;

/**
 * Representation of a dataSet which is stored in elasticsearch.
 *
 * @author René Reitmann
 */
public class DataSetSearchDocument extends DataSet {
  
  private StudySubDocumentProjection study;
  private List<VariableSubDocumentProjection> variables = 
      new ArrayList<VariableSubDocumentProjection>();
  private List<RelatedPublicationSubDocumentProjection> relatedPublications = 
      new ArrayList<RelatedPublicationSubDocumentProjection>();
  private List<SurveySubDocumentProjection> surveys = 
      new ArrayList<SurveySubDocumentProjection>();
  
  /**
   * Construct the search document with all related subdocuments.
   * @param dataSet The data set to be searched for.
   * @param study The study containing this data set.
   * @param variables The variables available in this data set.
   * @param relatedPublications The related publications using this data set.
   * @param surveys The surveys using this data set.
   */
  @SuppressWarnings("CPD-START")
  public DataSetSearchDocument(DataSet dataSet, 
      StudySubDocumentProjection study, 
      List<VariableSubDocumentProjection> variables,
      List<RelatedPublicationSubDocumentProjection> relatedPublications, 
      List<SurveySubDocumentProjection> surveys) {
    super(dataSet);
    this.study = study;      
    if (variables != null) {
      this.variables = variables;      
    }
    if (relatedPublications != null) {
      this.relatedPublications = relatedPublications;
    }
    if (surveys != null) {
      this.surveys = surveys;
    }
  }

  public StudySubDocumentProjection getStudy() {
    return study;
  }

  public List<VariableSubDocumentProjection> getVariables() {
    return variables;
  }

  public List<RelatedPublicationSubDocumentProjection> getRelatedPublications() {
    return relatedPublications;
  }

  public List<SurveySubDocumentProjection> getSurveys() {
    return surveys;
  }
}

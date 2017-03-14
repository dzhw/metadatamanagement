package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.projections.RelatedPublicationSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.projection.StudySubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.projections.SurveySubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.projections.VariableSubDocumentProjection;

/**
 * Representation of a dataSet which is stored in elasticsearch.
 *
 * @author René Reitmann
 */
public class DataSetSearchDocument extends DataSet {
  
  private StudySubDocument study = null;
  private List<VariableSubDocument> variables = 
      new ArrayList<VariableSubDocument>();
  private List<RelatedPublicationSubDocument> relatedPublications = 
      new ArrayList<RelatedPublicationSubDocument>();
  private List<SurveySubDocument> surveys = 
      new ArrayList<SurveySubDocument>();
  private Release release = null;
  
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
      List<SurveySubDocumentProjection> surveys,
      Release release) {
    super(dataSet);
    if (study != null) {
      this.study = new StudySubDocument(study);            
    }
    if (variables != null) {
      this.variables = variables.stream()
          .map(VariableSubDocument::new).collect(Collectors.toList());      
    }
    if (relatedPublications != null) {
      this.relatedPublications = relatedPublications.stream()
          .map(RelatedPublicationSubDocument::new).collect(Collectors.toList());
    }
    if (surveys != null) {
      this.surveys = surveys.stream()
          .map(SurveySubDocument::new).collect(Collectors.toList());
    }
    this.release = release;
  }

  public StudySubDocument getStudy() {
    return study;
  }

  public void setStudy(StudySubDocument study) {
    this.study = study;
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

  public Release getRelease() {
    return release;
  }

  public void setRelease(Release release) {
    this.release = release;
  }
}

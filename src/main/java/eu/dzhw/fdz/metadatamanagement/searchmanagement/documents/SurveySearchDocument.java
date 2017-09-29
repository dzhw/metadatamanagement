package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.projections.DataSetSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.projections.InstrumentSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.projections.QuestionSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.projections.RelatedPublicationSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.projection.StudySubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.projections.VariableSubDocumentProjection;

/**
 * Representation of a survey which is stored in elasticsearch.
 *
 * @author René Reitmann
 * @author Daniel Katzberg
 */
public class SurveySearchDocument extends Survey implements SearchDocumentInterface {
  private StudySubDocument study = null;
  private List<DataSetSubDocument> dataSets = 
      new ArrayList<>();
  private List<VariableSubDocument> variables =
      new ArrayList<>();
  private List<RelatedPublicationSubDocument> relatedPublications = 
      new ArrayList<>();
  private List<InstrumentSubDocument> instruments = 
      new ArrayList<>();
  private List<QuestionSubDocument> questions = 
      new ArrayList<>();
  private Release release = null;
  
  private I18nString guiLabels = SurveyDetailsGuiLabels.GUI_LABELS;
  
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
      List<QuestionSubDocumentProjection> questions,
      Release release) {
    super(survey);
    if (study != null) {
      this.study = new StudySubDocument(study);      
    }
    if (dataSets != null) {
      this.dataSets = dataSets.stream()
          .map(DataSetSubDocument::new).collect(Collectors.toList());      
    }
    if (variables != null) {
      this.variables = variables.stream()
          .map(VariableSubDocument::new).collect(Collectors.toList());      
    }
    if (relatedPublications != null) {
      this.relatedPublications = relatedPublications.stream()
          .map(RelatedPublicationSubDocument::new).collect(Collectors.toList());      
    }
    if (instruments != null) {
      this.instruments = instruments.stream()
          .map(InstrumentSubDocument::new).collect(Collectors.toList());      
    }
    if (questions != null) {
      this.questions = questions.stream()
          .map(QuestionSubDocument::new).collect(Collectors.toList());      
    }
    this.release = release;
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

  public Release getRelease() {
    return release;
  }

  public void setRelease(Release release) {
    this.release = release;
  }
  
  @Override
  public I18nString getGuiLabels() {
    return guiLabels;
  }

  public void setGuiLabels(I18nString guiLabels) {
    this.guiLabels = guiLabels;
  }
}

package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.util.List;
import java.util.stream.Collectors;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSetSubDocument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.InstrumentSubDocument;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublicationSubDocument;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.StudySubDocument;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.SurveySubDocument;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;

/**
 * Representation of a variable which is stored in elasticsearch.
 *
 * @author René Reitmann
 * @author Daniel Katzberg
 */
public class VariableSearchDocument extends Variable {
  private DataSetSubDocument dataSet;
  private StudySubDocument study;
  private List<RelatedPublicationSubDocument> relatedPublications;
  private List<SurveySubDocument> surveys;
  private List<InstrumentSubDocument> instruments; 
  
  /**
   * Construct the search document with all related subdocuments.
   * @param variable the variable to be searched for
   * @param dataSet the data set containing this variable
   * @param study the study containing this variable
   * @param relatedPublications the related publications using this variable
   * @param surveys the surveys using this variable
   * @param instruments the instruments using this variable
   */
  public VariableSearchDocument(Variable variable, DataSet dataSet, Study study,
      List<RelatedPublication> relatedPublications, List<Survey> surveys, 
      List<Instrument> instruments) {
    super(variable);
    if (dataSet != null) {      
      this.dataSet = new DataSetSubDocument(dataSet);
    }
    if (study != null) {
      this.study = new StudySubDocument(study);      
    }
    this.relatedPublications = relatedPublications.stream()
        .map(RelatedPublicationSubDocument::new).collect(Collectors.toList());
    this.surveys = surveys.stream().map(SurveySubDocument::new).collect(Collectors.toList());
    this.instruments = instruments.stream()
        .map(InstrumentSubDocument::new).collect(Collectors.toList());
  }

  public DataSetSubDocument getDataSet() {
    return dataSet;
  }

  public void setDataSet(DataSetSubDocument dataSet) {
    this.dataSet = dataSet;
  }

  public StudySubDocument getStudy() {
    return study;
  }

  public void setStudy(StudySubDocument study) {
    this.study = study;
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

  public List<InstrumentSubDocument> getInstruments() {
    return instruments;
  }

  public void setInstruments(List<InstrumentSubDocument> instruments) {
    this.instruments = instruments;
  }
}

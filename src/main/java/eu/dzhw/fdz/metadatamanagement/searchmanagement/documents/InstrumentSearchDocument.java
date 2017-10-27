package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.projections.DataSetSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.projections.QuestionSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.projections.RelatedPublicationSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.projection.StudySubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.projections.SurveySubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.projections.VariableSubDocumentProjection;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Representation of an instrument which is stored in elasticsearch.
 *
 * @author René Reitmann
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Setter
public class InstrumentSearchDocument extends Instrument implements SearchDocumentInterface {
  private StudySubDocument study = null;
  private List<SurveySubDocument> surveys = 
      new ArrayList<>();
  private List<QuestionSubDocument> questions = 
      new ArrayList<>();
  private List<VariableSubDocument> variables = 
      new ArrayList<>();
  private List<DataSetSubDocument> dataSets = 
      new ArrayList<>();
  private List<RelatedPublicationSubDocument> relatedPublications = 
      new ArrayList<>();
  private Release release = null;
  
  private I18nString guiLabels = InstrumentDetailsGuiLabels.GUI_LABELS;
  
  /**
   * Construct the search document with all related subdocuments.
   * @param instrument the instrument to be searched for
   * @param study the study containing this instrument
   * @param surveys the surveys using this intrument
   * @param questions the questions used by this instrument
   * @param variables the variables used by the questions of this instrument
   * @param relatedPublications the related publications using this instrument
   */
  @SuppressWarnings("CPD-START")
  public InstrumentSearchDocument(Instrument instrument, 
      StudySubDocumentProjection study, 
      List<SurveySubDocumentProjection> surveys,
      List<QuestionSubDocumentProjection> questions, 
      List<VariableSubDocumentProjection> variables,
      List<DataSetSubDocumentProjection> dataSets,
      List<RelatedPublicationSubDocumentProjection> relatedPublications,
      Release release) {
    super(instrument);
    if (study != null) {
      this.study = new StudySubDocument(study);      
    }
    if (surveys != null) {
      this.surveys = surveys.stream()
          .map(SurveySubDocument::new).collect(Collectors.toList()); 
    }
    if (questions != null) {
      this.questions = questions.stream()
          .map(QuestionSubDocument::new).collect(Collectors.toList());      
    }
    if (variables != null) {
      this.variables = variables.stream()
          .map(VariableSubDocument::new).collect(Collectors.toList());      
    }
    if (dataSets != null) {
      this.dataSets = dataSets.stream()
          .map(DataSetSubDocument::new).collect(Collectors.toList());      
    }
    if (relatedPublications != null) {
      this.relatedPublications = relatedPublications.stream()
          .map(RelatedPublicationSubDocument::new).collect(Collectors.toList());
    }
    this.release = release;
  }
}

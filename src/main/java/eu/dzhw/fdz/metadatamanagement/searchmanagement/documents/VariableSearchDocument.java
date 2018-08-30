package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.projections.DataSetSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.projections.InstrumentSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.projections.QuestionSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.projections.RelatedPublicationSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.projection.StudySubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.projections.SurveySubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Representation of a variable which is stored in elasticsearch.
 *
 * @author René Reitmann
 * @author Daniel Katzberg
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Setter
public class VariableSearchDocument extends Variable implements SearchDocumentInterface {
  private DataSetSubDocument dataSet = null;
  private DataSetNestedDocument nestedDataSet = null;
  private StudySubDocument study = null;
  private StudyNestedDocument nestedStudy = null;
  private List<RelatedPublicationSubDocument> relatedPublications = 
      new ArrayList<>();
  private List<QuestionNestedDocument> nestedQuestions = new ArrayList<>();
  private List<RelatedPublicationNestedDocument> nestedRelatedPublications = new ArrayList<>();
  private List<SurveySubDocument> surveys = 
      new ArrayList<>();
  private List<SurveyNestedDocument> nestedSurveys = new ArrayList<>();
  private List<InstrumentSubDocument> instruments = 
      new ArrayList<>();
  private List<InstrumentNestedDocument> nestedInstruments = new ArrayList<>();
  private Release release = null;
  
  private I18nString guiLabels = VariableDetailsGuiLabels.GUI_LABELS;
  
  private I18nString completeTitle;

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
      Map<String, InstrumentSubDocumentProjection> instruments,
      List<QuestionSubDocumentProjection> questions,
      Release release,
      String doi) {
    super(variable);
    if (dataSet != null) {
      this.dataSet = new DataSetSubDocument(dataSet);
      this.nestedDataSet = new DataSetNestedDocument(dataSet);
    }
    if (study != null) {
      this.study = new StudySubDocument(study, doi);
      this.nestedStudy = new StudyNestedDocument(study);
    }
    if (relatedPublications != null) {
      this.relatedPublications = relatedPublications.stream()
          .map(RelatedPublicationSubDocument::new).collect(Collectors.toList());
      this.nestedRelatedPublications = relatedPublications.stream()
          .map(RelatedPublicationNestedDocument::new).collect(Collectors.toList());
    }
    if (surveys != null) {
      this.surveys = surveys.stream()
          .map(SurveySubDocument::new).collect(Collectors.toList());
      this.nestedSurveys =
          surveys.stream().map(SurveyNestedDocument::new).collect(Collectors.toList());
    }
    if (instruments != null) {
      this.instruments = instruments.values().stream()
          .map(InstrumentSubDocument::new).collect(Collectors.toList());
      this.nestedInstruments =
          instruments.values().stream().map(InstrumentNestedDocument::new)
              .collect(Collectors.toList());
    }
    if (questions != null) {
      this.nestedQuestions = questions.stream().map(
          question -> new QuestionNestedDocument(question,
              instruments.get(question.getInstrumentId())))
          .collect(Collectors.toList());
    }
    this.release = release;
    this.completeTitle = I18nString.builder()
        .de((variable.getLabel().getDe() != null ? variable.getLabel().getDe()
            : variable.getLabel().getEn()) + " (" + variable.getId() + ")")
        .en((variable.getLabel().getEn() != null ? variable.getLabel().getEn()
            : variable.getLabel().getDe()) + " (" + variable.getId() + ")")
        .build();
  }
}

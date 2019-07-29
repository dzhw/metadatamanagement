package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.projections.ConceptSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.projections.DataSetSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.projections.InstrumentSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Configuration;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.projections.QuestionSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.projections.RelatedPublicationSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.projection.StudySubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.projections.VariableSubDocumentProjection;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Representation of a survey which is stored in elasticsearch.
 *
 * @author René Reitmann
 * @author Daniel Katzberg
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Setter
public class SurveySearchDocument extends Survey implements SearchDocumentInterface {

  private static final long serialVersionUID = -3963249178909573553L;
  
  private StudySubDocument study = null;
  private StudyNestedDocument nestedStudy = null;
  private List<DataSetSubDocument> dataSets = 
      new ArrayList<>();
  private List<DataSetNestedDocument> nestedDataSets = new ArrayList<>();
  private List<VariableSubDocument> variables =
      new ArrayList<>();
  private List<VariableNestedDocument> nestedVariables = new ArrayList<>();
  private List<RelatedPublicationSubDocument> relatedPublications = 
      new ArrayList<>();
  private List<RelatedPublicationNestedDocument> nestedRelatedPublications = new ArrayList<>();
  private List<InstrumentSubDocument> instruments = 
      new ArrayList<>();
  private List<InstrumentNestedDocument> nestedInstruments = new ArrayList<>();
  private List<QuestionSubDocument> questions = 
      new ArrayList<>();
  private List<QuestionNestedDocument> nestedQuestions = new ArrayList<>();
  private List<ConceptSubDocument> concepts = 
      new ArrayList<>();
  private List<ConceptNestedDocument> nestedConcepts = new ArrayList<>();
  private Release release = null;
  private Configuration configuration;
  
  private I18nString guiLabels = SurveyDetailsGuiLabels.GUI_LABELS;
  
  private I18nString completeTitle;

  /**
   * Construct the search document with all related subdocuments.
   * @param survey the survey to be searched for
   * @param study the study containing this survey
   * @param dataSets the data sets available for this survey
   * @param variables the variables available for this survey
   * @param relatedPublications the publication using this survey
   * @param instruments the instruments used by this survey
   * @param questions the questions used by this survey
   * @param concepts the concepts used by this survey
   * @param configuration the project configuration
   */
  @SuppressWarnings("CPD-START")
  public SurveySearchDocument(Survey survey,
                              StudySubDocumentProjection study,
                              List<DataSetSubDocumentProjection> dataSets,
                              List<VariableSubDocumentProjection> variables,
                              List<RelatedPublicationSubDocumentProjection> relatedPublications,
                              List<InstrumentSubDocumentProjection> instruments,
                              List<QuestionSubDocumentProjection> questions,
                              List<ConceptSubDocumentProjection> concepts,
                              Release release,
                              String doi,
                              Configuration configuration) {
    super(survey);
    if (study != null) {
      this.study = new StudySubDocument(study, doi);
      this.nestedStudy = new StudyNestedDocument(study);
    }
    if (dataSets != null) {
      this.dataSets = dataSets.stream()
          .map(DataSetSubDocument::new).collect(Collectors.toList());
      this.nestedDataSets =
          dataSets.stream().map(DataSetNestedDocument::new).collect(Collectors.toList());
    }
    if (variables != null) {
      this.variables = variables.stream()
          .map(VariableSubDocument::new).collect(Collectors.toList());
      this.nestedVariables =
          variables.stream().map(VariableNestedDocument::new).collect(Collectors.toList());
    }
    if (relatedPublications != null) {
      this.relatedPublications = relatedPublications.stream()
          .map(RelatedPublicationSubDocument::new).collect(Collectors.toList());
      this.nestedRelatedPublications = relatedPublications.stream()
          .map(RelatedPublicationNestedDocument::new).collect(Collectors.toList());
    }
    if (instruments != null) {
      this.instruments = instruments.stream()
          .map(InstrumentSubDocument::new).collect(Collectors.toList());
      this.nestedInstruments = instruments.stream().map(InstrumentNestedDocument::new)
          .collect(Collectors.toList());
    }
    if (questions != null) {
      this.questions = questions.stream()
          .map(question -> new QuestionSubDocument(question)).collect(Collectors.toList());
      this.nestedQuestions = questions.stream()
          .map(question -> new QuestionNestedDocument(question))
          .collect(Collectors.toList());
    }
    if (concepts != null) {
      this.concepts = concepts.stream()
          .map(concept -> new ConceptSubDocument(concept)).collect(Collectors.toList());
      this.nestedConcepts = concepts.stream()
          .map(concept -> new ConceptNestedDocument(concept))
          .collect(Collectors.toList());
    }
    this.release = release;
    this.configuration = configuration;
    this.completeTitle = I18nString.builder()
        .de((survey.getTitle().getDe() != null ? survey.getTitle().getDe()
            : survey.getTitle().getEn()) + " (" + survey.getId() + ")")
        .en((survey.getTitle().getEn() != null ? survey.getTitle().getEn()
            : survey.getTitle().getDe()) + " (" + survey.getId() + ")")
        .build();
  }
}

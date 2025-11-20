package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.projections.ConceptSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.projection.DataPackageSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.projections.DataSetSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.projections.InstrumentSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Configuration;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.projections.QuestionSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.projections.VariableSubDocumentProjection;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
public class SurveySearchDocument extends Survey implements SearchDocumentInterface {

  private static final long serialVersionUID = -3963249178909573553L;

  static final String[] FIELDS_TO_EXCLUDE_ON_DESERIALIZATION = new String[] {"nested*",
      "variables", "questions", "configuration", "guiLabels", "*Publications", "concepts"};

  private DataPackageSubDocument dataPackage = null;
  private DataPackageNestedDocument nestedDataPackage = null;
  private List<DataSetSubDocument> dataSets = new ArrayList<>();
  private List<DataSetNestedDocument> nestedDataSets = new ArrayList<>();
  private List<VariableSubDocument> variables = new ArrayList<>();
  private List<VariableNestedDocument> nestedVariables = new ArrayList<>();
  private List<InstrumentSubDocument> instruments = new ArrayList<>();
  private List<InstrumentNestedDocument> nestedInstruments = new ArrayList<>();
  private List<QuestionSubDocument> questions = new ArrayList<>();
  private List<QuestionNestedDocument> nestedQuestions = new ArrayList<>();
  private List<ConceptSubDocument> concepts = new ArrayList<>();
  private List<ConceptNestedDocument> nestedConcepts = new ArrayList<>();
  private Release release = null;
  private Configuration configuration;

  private I18nString guiLabels = SurveyDetailsGuiLabels.GUI_LABELS;

  private I18nString completeTitle;

  /**
   * Construct the search document with all related subdocuments.
   *
   * @param survey the survey to be searched for
   * @param dataPackage the dataPackage containing this survey
   * @param dataSets the data sets available for this survey
   * @param variables the variables available for this survey
   * @param instruments the instruments used by this survey
   * @param questions the questions used by this survey
   * @param concepts the concepts used by this survey
   * @param configuration the project configuration
   */
  @SuppressWarnings("CPD-START")
  public SurveySearchDocument(Survey survey, DataPackageSubDocumentProjection dataPackage,
      List<DataSetSubDocumentProjection> dataSets, List<VariableSubDocumentProjection> variables,
      List<InstrumentSubDocumentProjection> instruments,
      List<QuestionSubDocumentProjection> questions, List<ConceptSubDocumentProjection> concepts,
      Release release, String doi, Configuration configuration) {
    super(survey);
    if (dataPackage != null) {
      this.dataPackage = new DataPackageSubDocument(dataPackage, doi);
      this.nestedDataPackage = new DataPackageNestedDocument(dataPackage);
    }
    if (dataSets != null && !dataSets.isEmpty()) {
      this.dataSets = dataSets.stream().map(DataSetSubDocument::new).collect(Collectors.toList());
      this.nestedDataSets =
          dataSets.stream().map(DataSetNestedDocument::new).collect(Collectors.toList());
    }
    if (variables != null && !variables.isEmpty()) {
      this.variables =
          variables.stream().map(VariableSubDocument::new).collect(Collectors.toList());
      this.nestedVariables =
          variables.stream().map(VariableNestedDocument::new).collect(Collectors.toList());
    }
    if (instruments != null && !instruments.isEmpty()) {
      this.instruments =
          instruments.stream().map(InstrumentSubDocument::new).collect(Collectors.toList());
      this.nestedInstruments =
          instruments.stream().map(InstrumentNestedDocument::new).collect(Collectors.toList());
    }
    if (questions != null && !questions.isEmpty()) {
      this.questions = questions.stream().map(question -> new QuestionSubDocument(question))
          .collect(Collectors.toList());
      this.nestedQuestions = questions.stream()
          .map(question -> new QuestionNestedDocument(question)).collect(Collectors.toList());
    }
    if (concepts != null && !concepts.isEmpty()) {
      this.concepts = concepts.stream().map(concept -> new ConceptSubDocument(concept))
          .collect(Collectors.toList());
      this.nestedConcepts = concepts.stream().map(concept -> new ConceptNestedDocument(concept))
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

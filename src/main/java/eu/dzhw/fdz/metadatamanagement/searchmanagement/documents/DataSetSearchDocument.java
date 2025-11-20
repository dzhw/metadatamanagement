package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.projections.ConceptSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.projection.DataPackageSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.projections.InstrumentSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Configuration;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.projections.QuestionSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.projections.SurveySubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.projections.VariableSubDocumentProjection;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Representation of a dataSet which is stored in elasticsearch.
 *
 * @author René Reitmann
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
public class DataSetSearchDocument extends DataSet implements SearchDocumentInterface {

  private static final long serialVersionUID = 9158419346767595658L;

  static final String[] FIELDS_TO_EXCLUDE_ON_DESERIALIZATION = new String[] {"nested*",
      "variables", "questions", "configuration", "guiLabels", "*Publications", "concepts"};

  private DataPackageSubDocument dataPackage = null;
  private DataPackageNestedDocument nestedDataPackage = null;
  private List<VariableSubDocument> variables = new ArrayList<>();
  private List<VariableNestedDocument> nestedVariables = new ArrayList<>();
  private List<InstrumentSubDocument> instruments = new ArrayList<>();
  private List<InstrumentNestedDocument> nestedInstruments = new ArrayList<>();
  private List<QuestionSubDocument> questions = new ArrayList<>();
  private List<QuestionNestedDocument> nestedQuestions = new ArrayList<>();
  private List<SurveySubDocument> surveys = new ArrayList<>();
  private List<SurveyNestedDocument> nestedSurveys = new ArrayList<>();
  private List<ConceptSubDocument> concepts = new ArrayList<>();
  private List<ConceptNestedDocument> nestedConcepts = new ArrayList<>();
  private Release release = null;
  private Configuration configuration = null;

  private Integer maxNumberOfObservations;

  private List<String> accessWays;

  private I18nString guiLabels = DataSetDetailsGuiLabels.GUI_LABELS;

  private I18nString completeTitle;

  /**
   * Construct the search document with all related subdocuments.
   *
   * @param dataSet The data set to be searched for.
   * @param dataPackage The dataPackage containing this data set.
   * @param variables The variables available in this data set.
   * @param surveys The surveys using this data set.
   * @param instruments The instruments used to create this data set.
   * @param questions The questions used to create this data set.
   * @param configuration the configuration from data acquisition
   */
  @SuppressWarnings("CPD-START")
  public DataSetSearchDocument(DataSet dataSet, DataPackageSubDocumentProjection dataPackage,
      List<VariableSubDocumentProjection> variables,
      List<SurveySubDocumentProjection> surveys, List<InstrumentSubDocumentProjection> instruments,
      List<QuestionSubDocumentProjection> questions, List<ConceptSubDocumentProjection> concepts,
      Release release, String doi, Configuration configuration) {
    super(dataSet);
    if (dataPackage != null) {
      this.dataPackage = new DataPackageSubDocument(dataPackage, doi);
      this.nestedDataPackage = new DataPackageNestedDocument(dataPackage);
    }
    if (variables != null && !variables.isEmpty()) {
      this.variables =
          variables.stream().map(VariableSubDocument::new).collect(Collectors.toList());
      this.nestedVariables =
          variables.stream().map(VariableNestedDocument::new).collect(Collectors.toList());
    }
    if (surveys != null && !surveys.isEmpty()) {
      this.surveys = surveys.stream().map(SurveySubDocument::new).collect(Collectors.toList());
      this.nestedSurveys =
          surveys.stream().map(SurveyNestedDocument::new).collect(Collectors.toList());
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
    this.maxNumberOfObservations = dataSet.getSubDataSets().stream()
        .map(subDataSet -> subDataSet.getNumberOfObservations()).reduce(Integer::max).get();
    this.accessWays = dataSet.getSubDataSets().stream().map(subDataSet -> subDataSet.getAccessWay())
        .collect(Collectors.toList());
    this.release = release;
    this.configuration = configuration;
    this.completeTitle = I18nString.builder()
        .de((dataSet.getDescription().getDe() != null ? dataSet.getDescription().getDe()
            : dataSet.getDescription().getEn()) + " (" + dataSet.getId() + ")")
        .en((dataSet.getDescription().getEn() != null ? dataSet.getDescription().getEn()
            : dataSet.getDescription().getDe()) + " (" + dataSet.getId() + ")")
        .build();
  }
}

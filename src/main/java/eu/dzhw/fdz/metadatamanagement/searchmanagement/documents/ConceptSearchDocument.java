package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.Concept;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.projections.DataSetSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.projections.InstrumentSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.projections.QuestionSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.projections.SurveySubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.projections.VariableSubDocumentProjection;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Representation of a concept which is stored in elasticsearch.
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@SuppressWarnings("CPD-START")
public class ConceptSearchDocument extends Concept implements SearchDocumentInterface {

  private static final long serialVersionUID = -5890470705228357297L;

  static final String[] FIELDS_TO_EXCLUDE_ON_DESERIALIZATION =
      new String[] {"nested*", "configuration", "guiLabels"};

  private List<DataPackageSubDocument> dataPackages = new ArrayList<>();
  private List<DataPackageNestedDocument> nestedDataPackages = new ArrayList<>();
  private List<QuestionSubDocument> questions = new ArrayList<>();
  private List<QuestionNestedDocument> nestedQuestions = new ArrayList<>();
  private List<InstrumentSubDocument> instruments = new ArrayList<>();
  private List<InstrumentNestedDocument> nestedInstruments = new ArrayList<>();
  private List<SurveySubDocument> surveys = new ArrayList<>();
  private List<SurveyNestedDocument> nestedSurveys = new ArrayList<>();
  private List<DataSetSubDocument> dataSets = new ArrayList<>();
  private List<DataSetNestedDocument> nestedDataSets = new ArrayList<>();
  private List<VariableSubDocument> variables = new ArrayList<>();
  private List<VariableNestedDocument> nestedVariables = new ArrayList<>();

  // dummy string which ensures that related publications are always released
  private String release = "released";

  /*
   * dummy field which ensures that related publications can be found.
   */
  private boolean shadow = false;

  private I18nString guiLabels = ConceptDetailsGuiLabels.GUI_LABELS;

  private I18nString completeTitle;

  /**
   * Construct the search document with all related subdocuments.
   *
   * @param concept the concept to be searched for
   * @param dataPackages the dataPackages in which this concept was used
   * @param questions the questions in which this concept was observed
   * @param instruments the instruments which observed this concept
   * @param surveys the surveys for which observed this concept
   * @param dataSets the dataSets for which observed this concept
   * @param variables the variables for which observed this concept
   */
  @SuppressWarnings("CPD-START")
  public ConceptSearchDocument(Concept concept, List<DataPackageSubDocument> dataPackages,
      List<DataPackageNestedDocument> nestedDataPackages,
      List<QuestionSubDocumentProjection> questions,
      List<InstrumentSubDocumentProjection> instruments, List<SurveySubDocumentProjection> surveys,
      List<DataSetSubDocumentProjection> dataSets, List<VariableSubDocumentProjection> variables) {
    super(concept);
    if (dataPackages != null && !dataPackages.isEmpty()) {
      this.dataPackages = dataPackages;
    }
    if (nestedDataPackages != null && !nestedDataPackages.isEmpty()) {
      this.nestedDataPackages = nestedDataPackages;
    }
    if (questions != null && !questions.isEmpty()) {
      this.questions = questions.stream().map(question -> new QuestionSubDocument(question))
          .collect(Collectors.toList());
      this.nestedQuestions = questions.stream()
          .map(question -> new QuestionNestedDocument(question)).collect(Collectors.toList());
    }
    if (instruments != null && !instruments.isEmpty()) {
      this.instruments =
          instruments.stream().map(InstrumentSubDocument::new).collect(Collectors.toList());
      this.nestedInstruments =
          instruments.stream().map(InstrumentNestedDocument::new).collect(Collectors.toList());
    }
    if (surveys != null && !surveys.isEmpty()) {
      this.surveys = surveys.stream().map(SurveySubDocument::new).collect(Collectors.toList());
      this.nestedSurveys =
          surveys.stream().map(SurveyNestedDocument::new).collect(Collectors.toList());
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
    this.completeTitle =
        I18nString.builder().de(concept.getTitle().getDe() + " (" + concept.getId() + ")")
            .en(concept.getTitle().getEn() + " (" + concept.getId() + ")").build();
  }
}

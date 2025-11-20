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
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.projections.SurveySubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
public class VariableSearchDocument extends Variable implements SearchDocumentInterface {

  private static final long serialVersionUID = 9175808841240756548L;

  static final String[] FIELDS_TO_EXCLUDE_ON_DESERIALIZATION =
      new String[] {"nested*", "configuration", "guiLabels", "*Publications"};

  private DataSetSubDocument dataSet = null;
  private DataSetNestedDocument nestedDataSet = null;
  private DataPackageSubDocument dataPackage = null;
  private DataPackageNestedDocument nestedDataPackage = null;
  private List<QuestionNestedDocument> nestedQuestions = new ArrayList<>();
  private List<SurveySubDocument> surveys =
      new ArrayList<>();
  private List<SurveyNestedDocument> nestedSurveys = new ArrayList<>();
  private List<InstrumentSubDocument> instruments =
      new ArrayList<>();
  private List<InstrumentNestedDocument> nestedInstruments = new ArrayList<>();
  private List<ConceptSubDocument> concepts =
      new ArrayList<>();
  private List<ConceptNestedDocument> nestedConcepts = new ArrayList<>();
  private Release release = null;
  private Configuration configuration = null;

  private I18nString guiLabels = VariableDetailsGuiLabels.GUI_LABELS;

  private I18nString completeTitle;

  /**
   * Construct the search document with all related subdocuments.
   * @param variable the variable to be searched for
   * @param dataSet the data set containing this variable
   * @param dataPackage the dataPackage containing this variable
   * @param surveys the surveys using this variable
   * @param instruments the instruments using this variable
   * @param concepts the concepts covered by this variable
   * @param configuration the project configuration
   */
  @SuppressWarnings("CPD-START")
  public VariableSearchDocument(Variable variable,
                                DataSetSubDocumentProjection dataSet,
                                DataPackageSubDocumentProjection dataPackage,
                                List<SurveySubDocumentProjection> surveys,
                                List<InstrumentSubDocumentProjection> instruments,
                                List<QuestionSubDocumentProjection> questions,
                                List<ConceptSubDocumentProjection> concepts,
                                Release release,
                                String doi,
                                Configuration configuration) {
    super(variable);
    if (dataSet != null) {
      this.dataSet = new DataSetSubDocument(dataSet);
      this.nestedDataSet = new DataSetNestedDocument(dataSet);
    }
    if (dataPackage != null) {
      this.dataPackage = new DataPackageSubDocument(dataPackage, doi);
      this.nestedDataPackage = new DataPackageNestedDocument(dataPackage);
    }
    if (surveys != null && !surveys.isEmpty()) {
      this.surveys = surveys.stream()
          .map(SurveySubDocument::new).collect(Collectors.toList());
      this.nestedSurveys =
          surveys.stream().map(SurveyNestedDocument::new).collect(Collectors.toList());
    }
    if (instruments != null && !instruments.isEmpty()) {
      this.instruments = instruments.stream()
          .map(InstrumentSubDocument::new).collect(Collectors.toList());
      this.nestedInstruments =
          instruments.stream().map(InstrumentNestedDocument::new)
              .collect(Collectors.toList());
    }
    if (questions != null && !questions.isEmpty()) {
      this.nestedQuestions = questions.stream().map(
          question -> new QuestionNestedDocument(question))
          .collect(Collectors.toList());
    }
    if (concepts != null && !concepts.isEmpty()) {
      this.concepts = concepts.stream()
          .map(concept -> new ConceptSubDocument(concept)).collect(Collectors.toList());
      this.nestedConcepts = concepts.stream()
          .map(concept -> new ConceptNestedDocument(concept))
          .collect(Collectors.toList());
    }
    this.release = release;
    this.completeTitle = I18nString.builder()
        .de((variable.getLabel().getDe() != null ? variable.getLabel().getDe()
            : variable.getLabel().getEn()) + " (" + variable.getId() + ")")
        .en((variable.getLabel().getEn() != null ? variable.getLabel().getEn()
            : variable.getLabel().getDe()) + " (" + variable.getId() + ")")
        .build();
    this.configuration = configuration;
  }
}

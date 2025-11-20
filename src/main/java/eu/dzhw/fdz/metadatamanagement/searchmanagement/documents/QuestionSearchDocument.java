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
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.projections.SurveySubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.projections.VariableSubDocumentProjection;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Representation of an question which is stored in elasticsearch.
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@SuppressWarnings("CPD-START")
public class QuestionSearchDocument extends Question implements SearchDocumentInterface {

  private static final long serialVersionUID = -869861030740857451L;

  static final String[] FIELDS_TO_EXCLUDE_ON_DESERIALIZATION =
      new String[] {"nested*", "variables", "configuration", "guiLabels", "*Publications"};

  private DataPackageSubDocument dataPackage = null;
  private DataPackageNestedDocument nestedDataPackage = null;
  private InstrumentSubDocument instrument = null;
  private InstrumentNestedDocument nestedInstrument = null;
  private List<SurveySubDocument> surveys =
      new ArrayList<>();
  private List<SurveyNestedDocument> nestedSurveys = new ArrayList<>();
  private List<VariableSubDocument> variables =
      new ArrayList<>();
  private List<VariableNestedDocument> nestedVariables = new ArrayList<>();
  private List<DataSetSubDocument> dataSets =
      new ArrayList<>();
  private List<DataSetNestedDocument> nestedDataSets = new ArrayList<>();
  private List<ConceptSubDocument> concepts =
      new ArrayList<>();
  private List<ConceptNestedDocument> nestedConcepts = new ArrayList<>();
  private Release release = null;
  private Configuration configuration;

  private I18nString guiLabels = QuestionDetailsGuiLabels.GUI_LABELS;

  private I18nString completeTitle;

  /**
   * Construct the search document with all related subdocuments.
   * @param question the question to be searched for
   * @param dataPackage the dataPackage containing this question
   * @param instrument the instrument containing this question
   * @param surveys the surveys using this question
   * @param variables the variables used by this question
   * @param configuration the project configuration
   */
  public QuestionSearchDocument(Question question,
                                DataPackageSubDocumentProjection dataPackage,
                                InstrumentSubDocumentProjection instrument,
                                List<SurveySubDocumentProjection> surveys,
                                List<VariableSubDocumentProjection> variables,
                                List<DataSetSubDocumentProjection> dataSets,
                                List<ConceptSubDocumentProjection> concepts,
                                Release release,
                                String doi,
                                Configuration configuration) {
    super(question);
    if (dataPackage != null) {
      this.dataPackage = new DataPackageSubDocument(dataPackage, doi);
      this.nestedDataPackage = new DataPackageNestedDocument(dataPackage);
    }
    if (instrument != null) {
      this.instrument = new InstrumentSubDocument(instrument);
      this.nestedInstrument = new InstrumentNestedDocument(instrument);
    }
    if (surveys != null && !surveys.isEmpty()) {
      this.surveys = surveys.stream()
          .map(SurveySubDocument::new).collect(Collectors.toList());
      this.nestedSurveys =
          surveys.stream().map(SurveyNestedDocument::new).collect(Collectors.toList());
    }
    if (variables != null && !variables.isEmpty()) {
      this.variables = variables.stream()
          .map(VariableSubDocument::new).collect(Collectors.toList());
      this.nestedVariables =
          variables.stream().map(VariableNestedDocument::new).collect(Collectors.toList());
    }
    if (dataSets != null && !dataSets.isEmpty()) {
      this.dataSets = dataSets.stream()
          .map(DataSetSubDocument::new).collect(Collectors.toList());
      this.nestedDataSets =
          dataSets.stream().map(DataSetNestedDocument::new).collect(Collectors.toList());
    }
    if (concepts != null && !concepts.isEmpty()) {
      this.concepts = concepts.stream()
          .map(concept -> new ConceptSubDocument(concept)).collect(Collectors.toList());
      this.nestedConcepts = concepts.stream()
          .map(concept -> new ConceptNestedDocument(concept))
          .collect(Collectors.toList());
    }
    this.release = release;
    this.configuration = configuration;
    if (instrument != null) {
      this.completeTitle = I18nString.builder()
          .de("Frage " + question.getNumber() + ": "
              + (instrument.getTitle().getDe() != null ? instrument.getTitle().getDe()
                  : instrument.getTitle().getEn())
              + " (" + question.getId() + ")")
          .en("Question " + question.getNumber() + ": "
              + (instrument.getTitle().getEn() != null ? instrument.getTitle().getEn()
                  : instrument.getTitle().getDe())
              + " (" + question.getId() + ")")
          .build();
    } else {
      this.completeTitle =
          I18nString.builder().de("Frage " + question.getNumber() + ": " + question.getId())
              .en("Question " + question.getNumber() + ": " + question.getId()).build();
    }
  }
}

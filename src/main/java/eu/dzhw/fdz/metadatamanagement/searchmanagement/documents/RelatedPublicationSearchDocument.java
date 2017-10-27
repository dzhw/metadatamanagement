package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.projections.DataSetSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.projections.InstrumentSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.projections.QuestionSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.projection.StudySubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.projections.SurveySubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.projections.VariableSubDocumentProjection;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Representation of a related publication which is stored in elasticsearch.
 *
 * @author Daniel Katzberg
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Setter
public class RelatedPublicationSearchDocument extends RelatedPublication 
    implements SearchDocumentInterface {
  private List<StudySubDocument> studies = 
      new ArrayList<>();
  private List<QuestionSubDocument> questions = 
      new ArrayList<>();
  private List<InstrumentSubDocument> instruments = 
      new ArrayList<>();
  private List<SurveySubDocument> surveys = 
      new ArrayList<>();
  private List<DataSetSubDocument> dataSets = 
      new ArrayList<>();
  private List<VariableSubDocument> variables = 
      new ArrayList<>();
  
  // dummy string which ensures that related publications are always released
  private String release = "__";
  
  private I18nString guiLabels = RelatedPublicationDetailsGuiLabels.GUI_LABELS;
  
  /**
   * Construct the search document with all related subdocuments.
   * @param relatedPublication the related publication to be searched for
   * @param studies the studies for which the publication was published
   * @param questions the questions for which the publication was published
   * @param instruments the instruments for which the publication was published
   * @param surveys the surveys for which the publication was published
   * @param dataSets the dataSets for which the publication was published
   * @param variables the variables for which the publication was published
   */
  @SuppressWarnings("CPD-START")
  public RelatedPublicationSearchDocument(RelatedPublication relatedPublication,
      List<StudySubDocumentProjection> studies, 
      List<QuestionSubDocumentProjection> questions, 
      List<InstrumentSubDocumentProjection> instruments,
      List<SurveySubDocumentProjection> surveys, 
      List<DataSetSubDocumentProjection> dataSets, 
      List<VariableSubDocumentProjection> variables) {
    super(relatedPublication);
    if (studies != null) {
      this.studies = studies.stream()
          .map(StudySubDocument::new).collect(Collectors.toList());      
    }
    if (questions != null) {
      this.questions = questions.stream()
          .map(QuestionSubDocument::new).collect(Collectors.toList());      
    }
    if (instruments != null) {
      this.instruments = instruments.stream()
          .map(InstrumentSubDocument::new).collect(Collectors.toList());      
    }
    if (surveys != null) {
      this.surveys = surveys.stream()
          .map(SurveySubDocument::new).collect(Collectors.toList());      
    }
    if (dataSets != null) {
      this.dataSets = dataSets.stream()
          .map(DataSetSubDocument::new).collect(Collectors.toList());      
    }
    if (variables != null) {
      this.variables = variables.stream()
          .map(VariableSubDocument::new).collect(Collectors.toList());      
    }
  }
}

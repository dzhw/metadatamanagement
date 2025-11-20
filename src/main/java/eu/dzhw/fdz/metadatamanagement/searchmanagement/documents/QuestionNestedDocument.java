package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.projections.QuestionSubDocumentProjection;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * NESTED subdocument used for filtering by questions.
 *
 * @author René Reitmann
 */
@SuppressWarnings("CPD-START")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
public class QuestionNestedDocument extends AbstractNestedSubDocument {

  private static final long serialVersionUID = -8018921195951689419L;

  private String id;

  private String number;

  private I18nString questionText;

  private I18nString completeTitle;

  private String instrumentId;

  private String dataPackageId;

  private String masterId;

  private boolean shadow;

  private String successorId;

  /**
   * Create the subdocument.
   *
   * @param projection the projection coming from mongo.
   */
  public QuestionNestedDocument(QuestionSubDocumentProjection projection) {
    super();
    BeanUtils.copyProperties(projection, this);
    this.questionText = projection.getQuestionText();
    this.completeTitle = I18nString.builder()
        .de("Frage " + projection.getNumber() + ": "
            + (questionText.getDe() != null ? questionText.getDe() : questionText.getEn()) + " ("
            + projection.getId() + ")")
        .en("Question " + projection.getNumber() + ": "
            + (questionText.getEn() != null ? questionText.getEn() : questionText.getDe()) + " ("
            + projection.getId() + ")")
        .build();
  }
}

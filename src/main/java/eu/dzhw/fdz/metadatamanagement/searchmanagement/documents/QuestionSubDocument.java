package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import org.springframework.beans.BeanUtils;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.projections.QuestionSubDocumentProjection;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Attributes of a question which are stored in other search documents.
 *  
 * @author René Reitmann
 */
@SuppressWarnings("CPD-START")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Setter
public class QuestionSubDocument extends AbstractNestedSubDocument
    implements QuestionSubDocumentProjection {
  private String id;
  
  private String dataAcquisitionProjectId;
  
  private String instrumentId;
  
  private Integer instrumentNumber;
  
  private String number;
  
  private I18nString questionText;
  
  private I18nString topic;
  
  private I18nString completeTitle;

  public QuestionSubDocument() {
    super();
  }
  
  /**
   * Create the subdocument.
   * 
   * @param projection the projection coming from mongo.
   * @param instrumentTitle the instruments title, can be null.
   */
  public QuestionSubDocument(QuestionSubDocumentProjection projection, I18nString instrumentTitle) {
    super();
    BeanUtils.copyProperties(projection, this);
    if (instrumentTitle != null) {
      this.completeTitle = I18nString.builder()
          .de("Frage " + projection.getNumber() + ": "
              + (instrumentTitle.getDe() != null ? instrumentTitle.getDe()
                  : instrumentTitle.getEn())
              + " (" + projection.getId() + ")")
          .en("Question " + projection.getNumber() + ": "
              + (instrumentTitle.getEn() != null ? instrumentTitle.getEn()
                  : instrumentTitle.getDe())
              + " (" + projection.getId() + ")")
          .build();
    } else {
      this.completeTitle =
          I18nString.builder().de("Frage " + projection.getNumber() + ": " + projection.getId())
              .en("Question " + projection.getNumber() + ": " + projection.getId()).build();
    }
  }
}

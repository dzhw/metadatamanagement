package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import org.springframework.beans.BeanUtils;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.projections.InstrumentSubDocumentProjection;
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
public class QuestionNestedDocument extends AbstractNestedSubDocument {
  private String id;

  private String number;

  private I18nString instrumentTitle;

  private I18nString completeTitle;

  /**
   * Create the subdocument.
   *
   * @param projection the projection coming from mongo.
   * @param instrument the corresponding instrument, can be null.
   */
  public QuestionNestedDocument(QuestionSubDocumentProjection projection,
      InstrumentSubDocumentProjection instrument) {
    super();
    BeanUtils.copyProperties(projection, this);
    this.instrumentTitle = null;
    if (instrument != null) {
      this.instrumentTitle = instrument.getTitle();
    }
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

  /**
   * Create the subdocument.
   *
   * @param projection the projection coming from mongo.
   * @param instrument the corresponding instrument, can be null.
   */
  public QuestionNestedDocument(QuestionSubDocumentProjection projection, Instrument instrument) {
    super();
    BeanUtils.copyProperties(projection, this);
    this.instrumentTitle = null;
    if (instrument != null) {
      this.instrumentTitle = instrument.getTitle();
    }
    if (instrumentTitle != null) {
      this.completeTitle = I18nString
          .builder().de(
              "Frage " + projection.getNumber() + ": "
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

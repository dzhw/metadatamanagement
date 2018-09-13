package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import org.springframework.beans.BeanUtils;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.projections.SurveySubDocumentProjection;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * NESTED subdocument used for filtering by surveys.
 * 
 * @author René Reitmann
 */
@SuppressWarnings("CPD-START")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Setter
public class SurveyNestedDocument extends AbstractNestedSubDocument {

  private String id;

  private I18nString title;

  private I18nString completeTitle;

  private String studyId;

  /**
   * Create the subdocument.
   * 
   * @param projection The projection coming from mongo.
   */
  public SurveyNestedDocument(SurveySubDocumentProjection projection) {
    super();
    BeanUtils.copyProperties(projection, this);
    this.completeTitle =
        I18nString.builder()
            .de((projection.getTitle().getDe() != null ? projection.getTitle().getDe()
                : projection.getTitle().getEn()) + " (" + projection.getId() + ")")
            .en((projection.getTitle().getEn() != null ? projection.getTitle().getEn()
                : projection.getTitle().getDe()) + " (" + projection.getId() + ")")
            .build();
  }
}

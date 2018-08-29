package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import org.springframework.beans.BeanUtils;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.projections.RelatedPublicationSubDocumentProjection;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * NESTED Subdocumentused for filtering by related publications.
 * 
 * @author René Reitmann
 */
@SuppressWarnings("CPD-START")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Setter
public class RelatedPublicationNestedDocument extends AbstractNestedSubDocument {
  private String id;

  private String title;

  private String language;

  private I18nString completeTitle;

  /**
   * Create the subdocument.
   * 
   * @param projection The projection coming from mongo.
   */
  public RelatedPublicationNestedDocument(RelatedPublicationSubDocumentProjection projection) {
    super();
    BeanUtils.copyProperties(projection, this);
    this.completeTitle =
        I18nString.builder().de(projection.getTitle() + " (" + projection.getId() + ")")
            .en(projection.getTitle() + " (" + projection.getId() + ")").build();
  }
}

package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import org.springframework.beans.BeanUtils;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.projections.ConceptSubDocumentProjection;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * NESTED subdocument used for filtering by concepts.
 * 
 * @author René Reitmann
 */
@SuppressWarnings("CPD-START")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Setter
public class ConceptNestedDocument extends AbstractNestedSubDocument {
  private String id;

  private String title;

  private I18nString completeTitle;

  /**
   * Create the subdocument.
   * 
   * @param projection The projection coming from mongo.
   */
  public ConceptNestedDocument(ConceptSubDocumentProjection projection) {
    super();
    BeanUtils.copyProperties(projection, this);
    this.completeTitle =
        I18nString.builder().de(projection.getTitle().getDe() + " (" + projection.getId() + ")")
            .en(projection.getTitle().getEn() + " (" + projection.getId() + ")").build();
  }
}

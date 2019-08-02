package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import org.springframework.beans.BeanUtils;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.projections.RelatedPublicationSubDocumentProjection;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Attributes of a publication which are stored in other search documents.
 *  
 * @author René Reitmann
 */
@SuppressWarnings("CPD-START")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Setter
public class RelatedPublicationSubDocument extends AbstractRdcDomainObject
    implements RelatedPublicationSubDocumentProjection {
  
  private static final long serialVersionUID = 2517129836197799142L;

  private String id;
  
  private String doi;
  
  private String title;
  
  private String authors;
  
  private String language;

  public RelatedPublicationSubDocument() {
    super();
  }
  
  /**
   * Create the subdocument.
   * 
   * @param projection The projection coming from mongo.
   */
  public RelatedPublicationSubDocument(RelatedPublicationSubDocumentProjection projection) {
    super();
    BeanUtils.copyProperties(projection, this);
  }
}

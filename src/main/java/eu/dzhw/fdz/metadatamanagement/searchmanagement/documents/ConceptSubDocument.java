package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.util.List;

import org.springframework.beans.BeanUtils;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.Person;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.Concept;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.Tags;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.projections.ConceptSubDocumentProjection;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Attributes of a {@link Concept} which are stored in other search documents.
 *  
 * @author René Reitmann
 */
@SuppressWarnings("CPD-START")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Setter
public class ConceptSubDocument extends AbstractRdcDomainObject
    implements ConceptSubDocumentProjection {
  
  private static final long serialVersionUID = 8783547432896846685L;

  private String id;
  
  private String doi;
  
  private I18nString title;
  
  private List<Person> authors;
  
  private Tags tags;

  public ConceptSubDocument() {
    super();
  }
  
  /**
   * Create the subdocument.
   * 
   * @param projection The projection coming from mongo.
   */
  public ConceptSubDocument(ConceptSubDocumentProjection projection) {
    super();
    BeanUtils.copyProperties(projection, this);
  }
}

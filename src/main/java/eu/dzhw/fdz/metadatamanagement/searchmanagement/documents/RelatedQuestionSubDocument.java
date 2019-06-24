package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.projections.RelatedQuestionSubDocumentProjection;
import lombok.Getter;
import lombok.Setter;

/**
 * Attributes of a related question which are stored in other search documents.
 *  
 * @author René Reitmann
 */
@Getter
@Setter
public class RelatedQuestionSubDocument implements RelatedQuestionSubDocumentProjection {
  private String questionId;
  
  private String instrumentId;
}

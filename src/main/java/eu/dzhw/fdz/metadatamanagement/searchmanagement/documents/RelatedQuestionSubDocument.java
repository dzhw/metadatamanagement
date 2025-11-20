package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.io.Serializable;

import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.projections.RelatedQuestionSubDocumentProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Attributes of a related question which are stored in other search documents.
 *
 * @author René Reitmann
 */
@Getter
@Setter
@NoArgsConstructor
public class RelatedQuestionSubDocument
    implements RelatedQuestionSubDocumentProjection, Serializable {

  private static final long serialVersionUID = 5637721193773295974L;

  private String questionId;

  private String instrumentId;
}

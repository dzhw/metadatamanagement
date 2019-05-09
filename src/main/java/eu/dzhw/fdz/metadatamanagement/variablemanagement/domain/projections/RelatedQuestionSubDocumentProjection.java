package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.projections;

import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.RelatedQuestion;

/**
 * Subset of {@link RelatedQuestion} attributes which can be used in other search documents as sub
 * document and as projection for mongo.
 * 
 * @author René Reitmann
 */
public interface RelatedQuestionSubDocumentProjection {
  String getQuestionId();

  String getInstrumentId();
}

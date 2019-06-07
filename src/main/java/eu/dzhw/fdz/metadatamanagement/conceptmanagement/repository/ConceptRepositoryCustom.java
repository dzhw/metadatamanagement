package eu.dzhw.fdz.metadatamanagement.conceptmanagement.repository;

import java.util.Set;

/**
 * Interface for custom implementation of some query methods.
 */
public interface ConceptRepositoryCustom {

  /**
   * Fetches a list of question ids where a concept is referenced.
   * @param conceptId Id of concept to search
   * @return List of question ids
   */
  Set<String> findQuestionIdsByConceptId(String conceptId);

  /**
   * Fetches a list of instrument ids where a concept is referenced.
   * @param conceptId Id of concept to search
   * @return List of question ids
   */
  Set<String> findInstrumentIdsByConceptId(String conceptId);
}

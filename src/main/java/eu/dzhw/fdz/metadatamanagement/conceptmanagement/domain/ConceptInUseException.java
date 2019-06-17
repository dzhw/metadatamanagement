package eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain;

import lombok.Getter;

import java.util.Set;

/**
 * Thrown if a delete attempt was made while the Concept is referenced by an instance of
 * {@link eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument}
 * or {@link eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question}.
 */
public class ConceptInUseException extends RuntimeException {

  @Getter
  private final Set<String> instrumentIds;
  @Getter
  private final Set<String> questionIds;

  public ConceptInUseException(Set<String> instrumentIds, Set<String> questionIds) {
    this.instrumentIds = instrumentIds;
    this.questionIds = questionIds;
  }
}

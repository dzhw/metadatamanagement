package eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain;

import java.util.Set;

import lombok.Getter;

/**
 * Thrown if a delete attempt was made while the Concept is referenced by an instance of
 * {@link eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument}
 * or {@link eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question}.
 */
public class ConceptInUseException extends RuntimeException {

  private static final long serialVersionUID = -6974472294658484192L;
  
  @Getter
  private final Set<String> instrumentIds;
  @Getter
  private final Set<String> questionIds;

  public ConceptInUseException(Set<String> instrumentIds, Set<String> questionIds) {
    this.instrumentIds = instrumentIds;
    this.questionIds = questionIds;
  }
}

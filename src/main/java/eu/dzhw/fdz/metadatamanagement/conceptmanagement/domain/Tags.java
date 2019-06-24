package eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain;

import java.util.Set;

import javax.validation.constraints.NotEmpty;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Contains tags associated with a concept.
 */
@Data
@NoArgsConstructor
public class Tags {

  /**
   * German tags. At least one tag must be provided.
   */
  @NotEmpty(message = "concept-management.error.concept.tags.not-empty")
  private Set<String> de;

  /**
   * English tags. At least one tag must be provided.
   */
  @NotEmpty(message = "concept-management.error.concept.tags.not-empty")
  private Set<String> en;
}

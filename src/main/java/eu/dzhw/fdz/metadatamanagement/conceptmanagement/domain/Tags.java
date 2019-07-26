package eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain;

import java.io.Serializable;
import java.util.Set;

import javax.validation.constraints.NotEmpty;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Contains tags associated with a concept.
 */
@Data
@NoArgsConstructor
public class Tags implements Serializable {
  
  private static final long serialVersionUID = 2019192566828607087L;

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

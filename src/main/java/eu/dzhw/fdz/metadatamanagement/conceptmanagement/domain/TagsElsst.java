package eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain;

import java.io.Serializable;
import java.util.Set;

import javax.validation.constraints.NotEmpty;

import eu.dzhw.fdz.metadatamanagement.common.domain.Elsst;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Contains ELSST tags associated with a concept.
 */
@Data
@NoArgsConstructor
public class TagsElsst implements Serializable {

  private static final long serialVersionUID = 2019192566828607087L;

  /**
   * German tags. At least one tag must be provided.
   */
  @NotEmpty(message = "concept-management.error.concept.tagsElsst.not-empty")
  private Set<Elsst> de;

  /**
   * English tags. At least one tag must be provided.
   */
  @NotEmpty(message = "concept-management.error.concept.tagsElsst.not-empty")
  private Set<Elsst> en;
}

package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain;

import java.io.Serializable;
import java.util.Set;

import javax.validation.constraints.NotEmpty;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Contains tags associated with a dataPackage.
 */
@Data
@NoArgsConstructor
public class Tags implements Serializable {

  private static final long serialVersionUID = 72245732592890720L;

  /**
   * German tags. At least one tag must be provided.
   */
  @NotEmpty(message = "datapackagemanagement.error.dataPackage.tags.not-empty")
  private Set<String> de;

  /**
   * English tags (optional).
   */
  private Set<String> en;
}

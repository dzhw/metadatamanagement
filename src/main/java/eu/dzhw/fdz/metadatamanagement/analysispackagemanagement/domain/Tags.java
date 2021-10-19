package eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain;

import java.io.Serializable;
import java.util.Set;

import javax.validation.constraints.NotEmpty;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Contains tags associated with an analysis package.
 */
@Data
@NoArgsConstructor
public class Tags implements Serializable {
  
  private static final long serialVersionUID = 2019192566828607087L;

  /**
   * German tags. At least one tag must be provided.
   */
  @NotEmpty(message = "analysis-package-management.error.analysis-package.tags.not-empty")
  private Set<String> de;

  /**
   * English tags. At least one tag must be provided.
   */
  @NotEmpty(message = "analysis-package-management.error.analysis-package.tags.not-empty")
  private Set<String> en;
}

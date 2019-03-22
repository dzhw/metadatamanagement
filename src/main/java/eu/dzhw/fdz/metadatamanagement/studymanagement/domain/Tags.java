package eu.dzhw.fdz.metadatamanagement.studymanagement.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * Contains tags associated with a study.
 */
@Data
@NoArgsConstructor
public class Tags {

  /**
   * German tags. At least one tag must be provided.
   */
  @NotEmpty(message = "studymanagement.error.study.tags.not-empty")
  private List<String> de;

  /**
   * English tags (optional).
   */
  private List<String> en;
}

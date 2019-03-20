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
   * German tags.
   */
  @NotEmpty
  private List<String> de;

  /**
   * English tags.
   */
  private List<String> en;
}

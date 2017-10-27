package eu.dzhw.fdz.metadatamanagement.common.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class representing Strings that can be internationalized.
 * 
 * @author René Reitmann
 * @author Daniel Katzberg
 */
@Data
@NoArgsConstructor 
@AllArgsConstructor
@Builder
public class I18nString {
  private String de;

  private String en;
}

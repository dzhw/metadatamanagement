package eu.dzhw.fdz.metadatamanagement.common.domain;

import org.javers.core.metamodel.annotation.ValueObject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Strings that can be represented in English and German.
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@ValueObject
public class I18nString {
  /**
   * The german version of this string.
   */
  private String de;

  /**
   * The english version of this string.
   */
  private String en;
}

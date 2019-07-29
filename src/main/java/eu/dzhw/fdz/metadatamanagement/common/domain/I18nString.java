package eu.dzhw.fdz.metadatamanagement.common.domain;

import java.io.Serializable;

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
public class I18nString implements Serializable {

  private static final long serialVersionUID = -1719643047898392875L;

  /**
   * The german version of this string.
   */
  private String de;

  /**
   * The english version of this string.
   */
  private String en;
}

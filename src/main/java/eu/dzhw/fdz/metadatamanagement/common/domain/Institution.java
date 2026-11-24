package eu.dzhw.fdz.metadatamanagement.common.domain;

import org.javers.core.metamodel.annotation.ValueObject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Strings that can be represented in English and German.
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder(builderMethodName = "institutionBuilder")
@ValueObject
public class Institution extends I18nString {

  private static final long serialVersionUID = -1719643047898392875L;

  /**
   * Optional ROR identifier/url for institutions.
   */
  private String ror;

}
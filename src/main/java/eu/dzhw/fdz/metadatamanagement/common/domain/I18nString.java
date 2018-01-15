package eu.dzhw.fdz.metadatamanagement.common.domain;

import org.javers.core.metamodel.annotation.ValueObject;

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
@ValueObject
public class I18nString {
  private String de;

  private String en;
}

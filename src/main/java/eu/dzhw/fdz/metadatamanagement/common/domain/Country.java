package eu.dzhw.fdz.metadatamanagement.common.domain;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a country with it's 2-letter country code and it's display name in
 * german and english.
 */
@Data
@AllArgsConstructor
public class Country implements Serializable {

  private static final long serialVersionUID = 3863141558622792401L;
  
  private String code;
  private String de;
  private String en;
}

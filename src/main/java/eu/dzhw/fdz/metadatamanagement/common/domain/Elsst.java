package eu.dzhw.fdz.metadatamanagement.common.domain;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Contains attributes of an ELSST (European Language Social Science Thesaurus) entry.
 */
@Data
@NoArgsConstructor
public class Elsst implements Serializable {

  /**
   * Preferred label, e.g. "PRÄSIDENTIELLE REGIERUNGSSYSTEME".
   */
  private String prefLabel;

  /**
   * Array of alternative labels, e.g. [ "PRAESIDENTIALISMUS", "PRAESIDENTIELLE REGIERUNGSSYSTEME", … ].
   */
  private List<String> altLabel;

  /**
   * Local name/ ID of label, e.g. "7f02c81a-c38a-43ba-8a28-3118b24732a2"
   */
  private String localname;

}

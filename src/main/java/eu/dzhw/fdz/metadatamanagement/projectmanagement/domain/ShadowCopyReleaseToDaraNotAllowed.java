package eu.dzhw.fdz.metadatamanagement.projectmanagement.domain;

/**
 * Thrown if client attempts to release a shadowed project to dara.
 */
public class ShadowCopyReleaseToDaraNotAllowed extends IllegalArgumentException {
  private static final long serialVersionUID = -1157372006155286806L;
}

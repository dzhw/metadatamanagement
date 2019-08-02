package eu.dzhw.fdz.metadatamanagement.common.domain;

/**
 * Exception thrown if client tries to delete a shadowed domain object.
 */
public class ShadowCopyDeleteNotAllowedException extends IllegalArgumentException {

  private static final long serialVersionUID = 2776116860623528478L;
}

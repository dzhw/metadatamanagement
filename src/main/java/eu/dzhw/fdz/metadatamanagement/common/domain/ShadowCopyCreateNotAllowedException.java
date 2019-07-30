package eu.dzhw.fdz.metadatamanagement.common.domain;

/**
 * Exception that should be thrown if client tries to create a shadowed domain object.
 */
public class ShadowCopyCreateNotAllowedException extends IllegalArgumentException {

  private static final long serialVersionUID = 8703032968011674001L;
}

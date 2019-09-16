package eu.dzhw.fdz.metadatamanagement.projectmanagement.domain;

/**
 * Exception which is thrown when the user tries to unhide shadows which are already unhidden.
 * 
 * @author René Reitmann
 */
public class ShadowUnhidingNotAllowedException extends Exception {
  private static final long serialVersionUID = 6282804107553767675L;

  public ShadowUnhidingNotAllowedException(String message) {
    super(message);
  }
}

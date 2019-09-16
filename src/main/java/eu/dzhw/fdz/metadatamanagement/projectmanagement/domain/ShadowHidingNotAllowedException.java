package eu.dzhw.fdz.metadatamanagement.projectmanagement.domain;

/**
 * Exception which is thrown when the user tries to hide a master or the most recent shadow copy.
 * 
 * @author René Reitmann
 */
public class ShadowHidingNotAllowedException extends Exception {
  private static final long serialVersionUID = -7949019887169025105L;

  public ShadowHidingNotAllowedException(String message) {
    super(message);
  }
}

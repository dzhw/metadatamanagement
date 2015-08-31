package eu.dzhw.fdz.metadatamanagement.web.common.exceptions;

/**
 * The application is not in a valid state, if a document is not found. This exception will be
 * thrown in this case of matter.
 * 
 * @author Daniel Katzberg
 *
 */
public class DocumentNotFoundException extends IllegalStateException {

  /**
   * Default serial UID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The default constructor without an explaining message and a thrown cause.
   */
  public DocumentNotFoundException() {
    super();
  }

  /**
   * This constructor has a message that explain the exception.
   * 
   * @param message A detailed message.
   */
  public DocumentNotFoundException(String message) {
    super(message);
  }

  /**
   * The constructor with an explaining message and a thrown cause.
   * 
   * @param message A detailed message that explain the exception.
   * @param cause A {@code Throwable} cause, that indicates a null value. The null value should be
   *        in a valid way the document, but it was not found.
   */
  public DocumentNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * The constructor with a thrown cause.
   * 
   * @param cause A {@code Throwable} cause, that indicates a null value. The null value should be
   *        in a valid way the document, but it was not found.
   */
  public DocumentNotFoundException(Throwable cause) {
    super(cause);
  }

}

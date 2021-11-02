package eu.dzhw.fdz.metadatamanagement.authmanagement.service.exception;

import java.io.Serial;

/**
 * An Exception thrown when a Server response is invalid (e.g. the Status is not 200).
 */
public class InvalidResponseException extends Exception {
  @Serial
  private static final long serialVersionUID = -8022390017962579206L;

  /**
   * Create an InvalidResponseException with a message explaining the cause of the exception.
   *
   * @param message the explanation for the cause of this Exception
   */
  public InvalidResponseException(String message) {
    super(message);
  }
}

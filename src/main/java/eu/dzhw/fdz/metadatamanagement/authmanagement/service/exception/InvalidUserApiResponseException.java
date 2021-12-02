package eu.dzhw.fdz.metadatamanagement.authmanagement.service.exception;

import java.io.Serial;

/**
 * A RuntimeException thrown when a User API Server response is invalid (e.g. the Status is not
 * 200).
 */
public class InvalidUserApiResponseException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = -8022390017962579206L;

  /**
   * Create an InvalidResponseException with a message explaining the cause of the exception.
   *
   * @param message the explanation for the cause of this Exception
   */
  public InvalidUserApiResponseException(String message) {
    super(message);
  }
}

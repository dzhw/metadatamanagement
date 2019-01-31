package eu.dzhw.fdz.metadatamanagement.ordermanagement.domain;

/**
 * Orders with {@link OrderState#ORDERED} must not be updated. This exception
 * should be thrown whenever an update attempt is made on such orders.
 */
public class OrderAlreadyCompletedException extends IllegalArgumentException {

  /**
   * Creates a new exception.
   */
  public OrderAlreadyCompletedException() {
  }
}

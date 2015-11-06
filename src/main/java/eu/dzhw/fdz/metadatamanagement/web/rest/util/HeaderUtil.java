package eu.dzhw.fdz.metadatamanagement.web.rest.util;

import org.springframework.http.HttpHeaders;

/**
 * Utility class for http header creation.
 *
 */
public class HeaderUtil {

  /**
   * Creates a alert within the http headers.
   * @param message a given message of the alert
   * @param param the parameters for the alert.
   * @return Modified http headers.
   */
  public static HttpHeaders createAlert(String message, String param) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-metadatamanagementApp-alert", message);
    headers.add("X-metadatamanagementApp-params", param);
    return headers;
  }

  public static HttpHeaders createEntityCreationAlert(String entityName, String param) {
    return createAlert("metadatamanagementApp." + entityName + ".created", param);
  }

  public static HttpHeaders createEntityUpdateAlert(String entityName, String param) {
    return createAlert("metadatamanagementApp." + entityName + ".updated", param);
  }

  public static HttpHeaders createEntityDeletionAlert(String entityName, String param) {
    return createAlert("metadatamanagementApp." + entityName + ".deleted", param);
  }
}

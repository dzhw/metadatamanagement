package eu.dzhw.fdz.metadatamanagement.common.rest.util;

import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

/**
 * Utility class for http header creation.
 *
 */
public class HeaderUtil {

  public static HttpHeaders createAlert(String message) {
    return createAlert(message, null);
  }

  /**
   * Create alert header for the angular client.
   */
  public static HttpHeaders createAlert(String message, String param) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-metadatamanagementApp-alert", message);
    if (StringUtils.hasText(param)) {
      headers.add("X-metadatamanagementApp-params", param);
    }
    return headers;
  }

  public static HttpHeaders createEntityCreationAlert(String entityName, String param) {
    return createAlert(entityName + ".created", param);
  }

  public static HttpHeaders createEntityUpdateAlert(String entityName, String param) {
    return createAlert(entityName + ".updated", param);
  }

  public static HttpHeaders createEntityDeletionAlert(String entityName, String param) {
    return createAlert(entityName + ".deleted", param);
  }
}

package eu.dzhw.fdz.metadatamanagement.web.rest.util;

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
    
    public static HttpHeaders createAlert(String message, String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-metadatamanagementApp-alert", message);
        if (StringUtils.hasText(param)) {
          headers.add("X-metadatamanagementApp-params", param);          
        }
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

package eu.dzhw.fdz.metadatamanagement.common.rest.util;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;

/**
 * Utility class for handling pagination.
 */
public class PaginationUtil {

  /**
   * Create headers for pagination.
   */
  public static HttpHeaders generatePaginationHttpHeaders(Page<?> page, String baseUrl)
      throws URISyntaxException {

    HttpHeaders headers = new HttpHeaders();
    headers.add("X-Total-Count", "" + page.getTotalElements());
    String link = "";
    if ((page.getNumber() + 1) < page.getTotalPages()) {
      link =
          "<" + new URI(baseUrl + "?page=" + (page.getNumber() + 1) + "&size=" + page.getSize())
            .toString() + ">; rel=\"next\",";
    }
    // prev link
    if (page.getNumber() > 0) {
      link +=
          "<" + new URI(baseUrl + "?page=" + (page.getNumber() - 1) + "&size=" + page.getSize())
            .toString() + ">; rel=\"prev\",";
    }
    // last and first link
    link +=
        "<" + new URI(baseUrl + "?page=" + (page.getTotalPages() - 1) + "&size=" + page.getSize())
          .toString() + ">; rel=\"last\",";
    link += "<" + new URI(baseUrl + "?page=" + 0 + "&size=" + page.getSize())
        + ">; rel=\"first\"";
    headers.add(HttpHeaders.LINK, link);
    return headers;
  }
}

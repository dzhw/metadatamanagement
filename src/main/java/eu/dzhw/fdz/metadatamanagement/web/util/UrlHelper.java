package eu.dzhw.fdz.metadatamanagement.web.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Helper bean for manipulating URLs.
 * 
 * @author René Reitmann
 */
@Component
public class UrlHelper {

  /**
   * Helper method which returns the url including all requested query params.
   * 
   * @param request the HTTP request
   * @return URL including query params (e.g. http://localhost:8080/?search=test)
   */
  private final String getCompleteRequestedUrl(final HttpServletRequest request) {
    StringBuffer requestUrl = request.getRequestURL();
    String queryString = request.getQueryString();

    if (!StringUtils.isEmpty(queryString)) {
      requestUrl.append("?").append(queryString);
    }
    return requestUrl.toString();
  }

  /**
   * Returns the requested URL with https as protocol.
   * 
   * @param httpServletRequest The current request
   * @return the requested URL with https as protocol
   */
  public final String getSecureUrl(final HttpServletRequest httpServletRequest) {
    return UriComponentsBuilder.fromHttpUrl(getCompleteRequestedUrl(httpServletRequest))
        .scheme("https").toUriString();
  }
}

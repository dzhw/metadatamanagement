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
@Component("urlHelper")
public class UrlHelper {

  /**
   * Helper method which returns the url including all requested query params.
   * 
   * @param request the HTTP request
   * @return URL including query params (e.g. http://localhost:8080/?search=test)
   */
  public final String getCompleteRequestedUrl(final HttpServletRequest request) {
    StringBuffer requestUrl = request.getRequestURL();
    String queryString = request.getQueryString();

    if (!StringUtils.isEmpty(queryString)) {
      requestUrl.append("?").append(queryString);
    }
    return requestUrl.toString();
  }

  /**
   * Add or replace the given query param in the given url with the given values.
   * 
   * @param url The url to manipulate (e.g. http://localhost:8008/?locale=de_DE)
   * @param paramName The name of the param to replace (e.g. locale)
   * @param paramValues The values of the param (e.g. en_US)
   * @return The manipulated url (e.g. http://localhost:8080/?locale=en_US)
   */
  public final String replaceQueryParam(final String url, final String paramName,
      final Object... paramValues) {
    return UriComponentsBuilder.fromHttpUrl(url).replaceQueryParam(paramName, paramValues)
        .toUriString();
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

  /**
   * Return the requested URL with additional query param.
   * 
   * @param request The current request
   * @param paramName the additional param name
   * @param paramValues the values of the additional url param
   * @return the requested URL with additional query param
   */
  public final String getUrlWithAdditionalQueryParam(final HttpServletRequest request,
      final String paramName, final Object... paramValues) {
    return replaceQueryParam(getCompleteRequestedUrl(request), paramName, paramValues);
  }

}

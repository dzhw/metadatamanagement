package eu.dzhw.fdz.metadatamanagement.web.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Helper bean for manipulating URLs
 * 
 * @author reitmann
 */
@Component("urlHelper")
public class URLHelper {

    /**
     * Helper method which returns the url including all requested query params.
     * 
     * @param request
     *            the HTTP request
     * @return URL including query params (e.g.
     *         http://localhost:8080/?search=test)
     */
    public String getCompleteRequestedURL(HttpServletRequest request) {
	StringBuffer requestURL = request.getRequestURL();
	String queryString = request.getQueryString();

	if (!StringUtils.isEmpty(queryString)) {
	    requestURL.append("?").append(queryString);
	}
	return requestURL.toString();
    }

    /**
     * Add or replace the given query param in the given url with the given
     * values.
     * 
     * @param url
     *            The url to manipulate (e.g.
     *            http://localhost:8008/?locale=de_DE)
     * @param paramName
     *            The name of the param to replace (e.g. locale)
     * @param paramValues
     *            The values of the param (e.g. en_US)
     * @return The manipulated url (e.g. http://localhost:8080/?locale=en_US)
     */
    public String replaceQueryParam(String url, String paramName,
	    Object... paramValues) {
	return UriComponentsBuilder.fromHttpUrl(url)
		.replaceQueryParam(paramName, paramValues).toUriString();
    }

    public String getSecureUrl(HttpServletRequest httpServletRequest) {
	return UriComponentsBuilder
		.fromHttpUrl(getCompleteRequestedURL(httpServletRequest))
		.scheme("https").toUriString();
    }

    public String getURLWithAdditionalQueryParam(HttpServletRequest request,
	    String paramName, Object... paramValues) {
	return replaceQueryParam(getCompleteRequestedURL(request), paramName,
		paramValues);
    }

}

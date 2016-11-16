package eu.dzhw.fdz.metadatamanagement.common.rest.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

/**
 * Filter which returns index.html (the angular app) for all
 * requests which need to be interpreted by the client.
 * 
 * @author René Reitmann
 */
public class AngularUrlsFilter implements Filter {

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    // Nothing to initialize
  }

  @Override
  public void destroy() {
    // Nothing to destroy
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    if (!(request instanceof HttpServletRequest)) {
      return;
    }

    HttpServletRequest httpRequest = (HttpServletRequest) request;
    String contextPath = httpRequest.getContextPath();
    String requestUri = httpRequest.getRequestURI();
    requestUri = StringUtils.substringAfter(requestUri, contextPath);
    if (requestUri.startsWith("/de/") || requestUri.startsWith("/en/") 
        || requestUri.equals("/de") || requestUri.equals("/en")) {
      //return index.html for all requests to angular views
      requestUri = "/index.html";
    }
    request.getRequestDispatcher(requestUri)
      .forward(request, response);
  }
}

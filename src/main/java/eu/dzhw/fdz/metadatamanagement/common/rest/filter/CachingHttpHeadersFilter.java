package eu.dzhw.fdz.metadatamanagement.common.rest.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;

/**
 * This filter is used in test, dev and prod to put HTTP cache headers with a 
 * long (1 month) expiration time for static resources (in /dist). 
 */
public class CachingHttpHeadersFilter implements Filter {

  // We consider the last modified date is the start up time of the server
  private static final long LAST_MODIFIED = System.currentTimeMillis();
  

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }

  @Override
  public void destroy() {
    // Nothing to destroy
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    if (response instanceof HttpServletResponse && request instanceof HttpServletRequest) {
      HttpServletRequest httpRequest = (HttpServletRequest) request;
      HttpServletResponse httpResponse = (HttpServletResponse) response;      
      String requestUri = httpRequest.getRequestURI();
      if (requestUri.endsWith("index.html")) {
        // index.html can be cached but must be revalidated
        httpResponse.setHeader(HttpHeaders.CACHE_CONTROL, 
            "max-age=0, must-revalidate, public");
      } else {
        httpResponse.setHeader(HttpHeaders.CACHE_CONTROL, 
            "max-age=2629000, must-revalidate, public");        
      }
      httpResponse.setHeader(HttpHeaders.PRAGMA, "cache");
      
      // Setting the Last-Modified and etag header, for browser caching
      httpResponse.setDateHeader(HttpHeaders.LAST_MODIFIED, LAST_MODIFIED);
      httpResponse.setHeader(HttpHeaders.ETAG, String.valueOf(LAST_MODIFIED));
      
      chain.doFilter(request, response);
    }
  }
}

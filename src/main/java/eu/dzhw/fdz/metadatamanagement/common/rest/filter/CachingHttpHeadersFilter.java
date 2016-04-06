package eu.dzhw.fdz.metadatamanagement.common.rest.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * This filter is used in production, to put HTTP cache headers with a long (1 month) expiration
 * time.
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

    if (response instanceof HttpServletResponse) {
      HttpServletResponse httpResponse = (HttpServletResponse) response;      
      httpResponse.setHeader("Cache-Control", "max-age=0, must-revalidate");
      httpResponse.setHeader("Pragma", "cache");
      
      // Setting the Last-Modified header, for browser caching
      httpResponse.setDateHeader("Last-Modified", LAST_MODIFIED);
      
      chain.doFilter(request, response);
    }
  }
}

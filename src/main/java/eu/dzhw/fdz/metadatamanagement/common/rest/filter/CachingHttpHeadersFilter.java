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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import eu.dzhw.fdz.metadatamanagement.common.config.Constants;

/**
 * This filter sets HTTP cache headers depending on the current profile. For prod, test and dev it
 * sets a long (1 month) expiration time for static resources (in /dist).
 */
public class CachingHttpHeadersFilter implements Filter {

  // We consider the last modified date is the start up time of the server
  private static final long LAST_MODIFIED = System.currentTimeMillis();

  @Autowired
  private Environment env;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
        filterConfig.getServletContext());
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
      if (env.acceptsProfiles(Constants.SPRING_PROFILE_LOCAL)) {
        httpResponse.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
        httpResponse.setHeader(HttpHeaders.PRAGMA, "no-cache");
        httpResponse.setDateHeader(HttpHeaders.EXPIRES, LAST_MODIFIED);
      } else {
        if (requestUri.endsWith("index.html")) {
          // index.html can be cached but must be revalidated
          httpResponse.setHeader(HttpHeaders.CACHE_CONTROL, "max-age=0, must-revalidate, public");
        } else {
          httpResponse.setHeader(HttpHeaders.CACHE_CONTROL,
              "max-age=2629000, must-revalidate, public");
        }
        httpResponse.setHeader(HttpHeaders.PRAGMA, "cache");
      }

      chain.doFilter(request, response);
    }
  }
}

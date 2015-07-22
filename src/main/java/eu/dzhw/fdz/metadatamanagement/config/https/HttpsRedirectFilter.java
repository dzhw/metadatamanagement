package eu.dzhw.fdz.metadatamanagement.config.https;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.dzhw.fdz.metadatamanagement.web.util.UrlHelper;

/**
 * Filter which redirects http requests to https when proxy header is not present.
 * 
 * @author René Reitmann
 */
public class HttpsRedirectFilter implements Filter {
  private UrlHelper urlHelper;

  public HttpsRedirectFilter(UrlHelper urlHelper) {
    this.urlHelper = urlHelper;
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    // nothing todo
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    if (request instanceof HttpServletRequest) {
      HttpServletRequest httpServletRequest = (HttpServletRequest) request;
      if (httpServletRequest.getHeader("X-Forwarded-Proto") != null) {
        String forwardedProtocol = httpServletRequest.getHeader("X-Forwarded-Proto");
        if (!forwardedProtocol.equals("https")) {
          HttpServletResponse httpServletResponse = (HttpServletResponse) response;
          httpServletResponse.sendRedirect(urlHelper.getSecureUrl(httpServletRequest));
        } else {
          chain.doFilter(request, response);
        }
      } else {
        throw new IllegalStateException("No http header 'X-Forwarded-Proto' found on cloudfoundry! "
            + "Are you really running in the cloud?");
      }
    }
  }

  @Override
  public void destroy() {
    // nothing todo
  }
}

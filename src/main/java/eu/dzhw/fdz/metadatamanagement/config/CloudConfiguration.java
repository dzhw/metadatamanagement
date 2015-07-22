package eu.dzhw.fdz.metadatamanagement.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import eu.dzhw.fdz.metadatamanagement.web.util.UrlHelper;

/**
 * Spring configuration which instantiates some beans if and only if the container is running on
 * cloudfoundry.
 * 
 * @author René Reitmann
 */
@Configuration
@Profile("cloud")
public class CloudConfiguration {

  /**
   * Register a filter which redirects any http request to https. Since ssl is being offloaded at
   * the load balancer we need to check the custom http header 'x-forwarded-proto'.
   * 
   * @param urlHelper Helper for constructing the redirect url
   * @return The filter which redirects to https.
   */
  @Bean
  public Filter httpsRedirectFilter(UrlHelper urlHelper) {
    return new HttpsRedirectFilter(urlHelper);
  }

  class HttpsRedirectFilter implements Filter {
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
          throw new IllegalStateException(
              "No http header 'X-Forwarded-Proto' found on cloudfoundry! "
                  + "Are you really running in the cloud?");
        }
      }
    }

    @Override
    public void destroy() {
      // nothing todo
    }
  };
}

package eu.dzhw.fdz.metadatamanagement.web.filter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.env.Environment;

/**
 * This filter is used in production, to put HTTP cache headers with a long (1 month) expiration
 * time.
 */
public class CachingHttpHeadersFilter implements Filter {

  // We consider the last modified date is the start up time of the server
  private static final long LAST_MODIFIED = System.currentTimeMillis();

  private long cacheTimeToLive = TimeUnit.DAYS.toMillis(31L);

  private Environment env;

  public CachingHttpHeadersFilter(Environment env) {
    this.env = env;
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    cacheTimeToLive = TimeUnit.DAYS
      .toMillis(env.getProperty("jhipster.http.cache.timeToLiveInDays", Long.class, 31L));
  }

  @Override
  public void destroy() {
    // Nothing to destroy
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    HttpServletResponse httpResponse = (HttpServletResponse) response;

    httpResponse.setHeader("Cache-Control", "max-age=" + cacheTimeToLive + ", public");
    httpResponse.setHeader("Pragma", "cache");

    // Setting Expires header, for proxy caching
    httpResponse.setDateHeader("Expires", cacheTimeToLive + System.currentTimeMillis());

    // Setting the Last-Modified header, for browser caching
    httpResponse.setDateHeader("Last-Modified", LAST_MODIFIED);

    chain.doFilter(request, response);
  }
}

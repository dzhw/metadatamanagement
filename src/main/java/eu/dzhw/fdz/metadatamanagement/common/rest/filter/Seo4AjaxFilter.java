package eu.dzhw.fdz.metadatamanagement.common.rest.filter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.common.io.ByteStreams;

import eu.dzhw.fdz.metadatamanagement.common.config.Constants;
import eu.dzhw.fdz.metadatamanagement.common.config.MetadataManagementProperties;

/**
 * Filter that forwards all requests coming from bots to SEO4Ajax. Only active on test and prod.
 * 
 * @author René Reitmann
 */
@Component
@Profile({Constants.SPRING_PROFILE_TEST, Constants.SPRING_PROFILE_PROD})
@Order(Ordered.HIGHEST_PRECEDENCE)
public class Seo4AjaxFilter extends OncePerRequestFilter {
  private static final int PROXY_READ_TIMEOUT = 10 * 1000;

  private static final int PROXY_CONNECT_TIMEOUT = 30 * 1000;

  private static final String USER_AGENT_HEADER = "User-Agent";

  private static final String ESCAPED_FRAGMENT_QUERY_PARAM = "_escaped_fragment_=";

  private String siteToken;

  private String regexpBots =
      ".*(bot|spider|pinterest|crawler|archiver|flipboard|mediapartners|facebookexternalhit|quora|"
          + "whatsapp|slack|twitter|outbrain|yahoo! slurp|embedly|"
          + "developers.google.com\\/+\\/web\\/snippet|vkshare|"
          + "w3c_validator|tumblr|skypeuripreview|nuzzel|qwantify|bitrix link preview|"
          + "xing-contenttabreceiver|chrome-lighthouse|mail\\.ru).*";

  private String urlApi = "https://api.seo4ajax.com/";

  /**
   * Create the filter with the configuration properties.
   * 
   * @throws ServletException if site token is missing
   */
  public Seo4AjaxFilter(MetadataManagementProperties properties) throws ServletException {
    siteToken = properties.getSeo4ajax().getSiteToken();
    if (StringUtils.isEmpty(siteToken)) {
      throw new ServletException("Seo4ajax: siteToken not set!");
    }
    if (!StringUtils.isEmpty(properties.getSeo4ajax().getRegexpBots())) {
      regexpBots = properties.getSeo4ajax().getRegexpBots();
    }
    if (!StringUtils.isEmpty(properties.getSeo4ajax().getApiUrl())) {
      urlApi = properties.getSeo4ajax().getApiUrl();
    }
    urlApi += siteToken;
  }

  @Override
  public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain) throws IOException, ServletException {
    String queryString = request.getQueryString();
    HttpsURLConnection urlConnection = null;
    boolean foundEscapedFragment = false;
    String url = urlApi + request.getRequestURI();
    if (queryString != null) {
      url += "?" + queryString;
      foundEscapedFragment = queryString.startsWith(ESCAPED_FRAGMENT_QUERY_PARAM);
    }
    if (foundEscapedFragment) {
      urlConnection = (HttpsURLConnection) new URL(url).openConnection();
    } else {
      String userAgent = request.getHeader(USER_AGENT_HEADER);
      if (userAgent != null && userAgent.toLowerCase(Locale.US).matches(regexpBots)) {
        String path = request.getRequestURI().substring(request.getContextPath().length());
        if (StringUtils.isEmpty(path) || path.equals("/") || path.startsWith("/de/")
            || path.startsWith("/en/") || path.endsWith("/en") || path.endsWith("/de"))
          urlConnection = (HttpsURLConnection) new URL(url).openConnection();
      }
    }
    if (urlConnection == null) {
      chain.doFilter(request, response);
    } else {
      urlConnection.setInstanceFollowRedirects(false);
      urlConnection.setConnectTimeout(PROXY_CONNECT_TIMEOUT);
      urlConnection.setReadTimeout(PROXY_READ_TIMEOUT);
      response.setStatus(urlConnection.getResponseCode());
      for (String headerName : urlConnection.getHeaderFields().keySet()) {
        response.addHeader(headerName, urlConnection.getHeaderField(headerName));
      }
      copy(urlConnection.getInputStream(), response.getOutputStream(), true);
    }
  }

  private void copy(InputStream inputStream, OutputStream outputStream, boolean closeInputStream)
      throws IOException {
    try {
      ByteStreams.copy(inputStream, outputStream);

      if (closeInputStream) {
        inputStream.close();
      }
    } finally {
      if (closeInputStream) {
        inputStream.close();
      }
    }
  }
}

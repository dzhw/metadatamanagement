package eu.dzhw.fdz.metadatamanagement.usermanagement.config;

import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

import jakarta.servlet.http.HttpServletRequest;

/**
 * {@link RequestMatcher} for ant style patterns with a {@code **} wildcard in the middle of the
 * path (e.g. {@code /api/search/**}{@code /_search}).
 *
 * <p>Spring Security deprecated {@code AntPathRequestMatcher} in favour of
 * {@code PathPatternRequestMatcher}, but {@code PathPattern} only allows {@code **} at the end of
 * a pattern. This matcher keeps the exact semantics of the default
 * {@code AntPathRequestMatcher} (case sensitive, path = servlet path + path info), so the
 * existing public endpoints stay reachable exactly as before.</p>
 */
final class AntStylePathRequestMatcher implements RequestMatcher {

  private static final AntPathMatcher MATCHER = createMatcher();

  private final HttpMethod httpMethod;

  private final String pattern;

  private AntStylePathRequestMatcher(HttpMethod httpMethod, String pattern) {
    this.httpMethod = httpMethod;
    this.pattern = pattern;
  }

  static AntStylePathRequestMatcher antPath(String pattern) {
    return new AntStylePathRequestMatcher(null, pattern);
  }

  static AntStylePathRequestMatcher antPath(HttpMethod httpMethod, String pattern) {
    return new AntStylePathRequestMatcher(httpMethod, pattern);
  }

  @Override
  public boolean matches(HttpServletRequest request) {
    if (this.httpMethod != null && StringUtils.hasText(request.getMethod())
        && !this.httpMethod.matches(request.getMethod())) {
      return false;
    }
    return MATCHER.match(this.pattern, getRequestPath(request));
  }

  private static String getRequestPath(HttpServletRequest request) {
    String url = request.getServletPath();
    String pathInfo = request.getPathInfo();
    if (pathInfo != null) {
      url = StringUtils.hasLength(url) ? url + pathInfo : pathInfo;
    }
    return url;
  }

  private static AntPathMatcher createMatcher() {
    AntPathMatcher matcher = new AntPathMatcher();
    matcher.setTrimTokens(false);
    matcher.setCaseSensitive(true);
    return matcher;
  }

  @Override
  public String toString() {
    return "AntStylePath [pattern='" + this.pattern + "'"
        + (this.httpMethod != null ? ", " + this.httpMethod : "") + "]";
  }
}

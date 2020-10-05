package eu.dzhw.fdz.metadatamanagement.common.rest.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filter which internally forwards request to legacy endpoints to current endpoints.
 * /studies/ -> /data-packages/
 *
 * @author René Reitmann
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LegacyUrlsFilter extends OncePerRequestFilter {
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    if (request.getRequestURI().contains("/studies/")
        || request.getRequestURI().endsWith("/studies")) {
      String contextPath = request.getContextPath();
      String requestUri = request.getRequestURI();
      requestUri = StringUtils.substringAfter(requestUri, contextPath);
      String newUri = requestUri.replace("/studies", "/data-packages");
      request.getRequestDispatcher(newUri).forward(request, response);
    } else {
      filterChain.doFilter(request, response);
    }
  }
}

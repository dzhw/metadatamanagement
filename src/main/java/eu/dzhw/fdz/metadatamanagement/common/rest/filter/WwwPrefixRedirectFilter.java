package eu.dzhw.fdz.metadatamanagement.common.rest.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Filter which intercepts requests to uris starting with www and redirects them to
 * the uri without www.
 * 
 * @author René Reitmann
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WwwPrefixRedirectFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    if (request.getServerName().startsWith("www.")) {
      ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromRequest(request);
      builder.host(request.getServerName().replaceFirst("www.", ""));
      response.setStatus(HttpStatus.MOVED_PERMANENTLY.value());
      response.setHeader(HttpHeaders.LOCATION, builder.toUriString());
    } else {
      filterChain.doFilter(request, response);
    }
  }
}


package eu.dzhw.fdz.metadatamanagement.common.rest.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filter which maps all client urls to the root (index.html).
 * 
 * @author René Reitmann
 */
@Component
public class ForwardClientUrlsToIndexFilter extends OncePerRequestFilter {

  @Override
  public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain) throws IOException, ServletException {
    String path = request.getRequestURI().substring(request.getContextPath().length());
    if (path.startsWith("/de/") || path.startsWith("/en/") || path.equals("/en")
        || path.equals("/de")) {
      String newUri = request.getRequestURI().replace(path, "/");
      request.getRequestDispatcher(newUri).forward(request, response);
    } else {
      chain.doFilter(request, response);
    }

  }
}

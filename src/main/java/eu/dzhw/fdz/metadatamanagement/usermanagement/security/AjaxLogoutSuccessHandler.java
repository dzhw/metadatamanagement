package eu.dzhw.fdz.metadatamanagement.usermanagement.security;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import eu.dzhw.fdz.metadatamanagement.usermanagement.service.MongoDbOAuth2AuthorizationService;
import lombok.RequiredArgsConstructor;

/**
 * Spring Security logout handler, specialized for Ajax requests.
 */
@Component
@RequiredArgsConstructor
public class AjaxLogoutSuccessHandler extends AbstractAuthenticationTargetUrlRequestHandler
    implements LogoutSuccessHandler {

  public static final String BEARER_AUTHENTICATION = "Bearer ";

  private final MongoDbOAuth2AuthorizationService authService;

  @Override
  public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {

    // Request the token
    String token = request.getHeader("authorization");
    if (token != null && token.startsWith(BEARER_AUTHENTICATION)) {
        OAuth2Authorization authorization = authService.findByToken(StringUtils.substringAfter(token, BEARER_AUTHENTICATION),
          OAuth2TokenType.ACCESS_TOKEN);

      if (authorization != null) {
        authService.remove(authorization);
      }
    }

    response.setStatus(HttpServletResponse.SC_OK);
  }
}

package eu.dzhw.fdz.metadatamanagement.usermanagement.security;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public final class PasswordGrantAuthenticationConverter implements AuthenticationConverter {

  private static final String USERNAME_PARAMETER = "username";
  private static final String PASSWORD_PARAMETER = "password";

  @Override
  public Authentication convert(HttpServletRequest request) {
    if (!PasswordGrantAuthenticationToken.GRANT_TYPE.getValue()
        .equals(request.getParameter(OAuth2ParameterNames.GRANT_TYPE))) {
      return null;
    }

    String username = request.getParameter(USERNAME_PARAMETER);
    String password = request.getParameter(PASSWORD_PARAMETER);
    if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
      throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.INVALID_REQUEST,
          "username and password are required", null));
    }

    String scopeParameter = request.getParameter(OAuth2ParameterNames.SCOPE);
    Set<String> scopes = StringUtils.hasText(scopeParameter)
        ? Arrays.stream(scopeParameter.trim().split("\\s+"))
            .filter(StringUtils::hasText)
            .collect(Collectors.toSet())
        : Set.of();

    return new PasswordGrantAuthenticationToken(
        SecurityContextHolder.getContext().getAuthentication(), username, password, scopes);
  }
}
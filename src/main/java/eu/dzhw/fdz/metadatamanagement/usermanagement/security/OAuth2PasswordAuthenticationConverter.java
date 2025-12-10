package eu.dzhw.fdz.metadatamanagement.usermanagement.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class OAuth2PasswordAuthenticationConverter implements AuthenticationConverter {

  @Override
  public Authentication convert(HttpServletRequest request) {
    var grantType = request.getParameter("grant_type");
    if (!"password".equals(grantType)) {
      return null;
    }

    var username = request.getParameter("username");
    var password = request.getParameter("password");

    var clientPrincipal = (Authentication) request.getUserPrincipal();

    var params = new HashMap<String, Object>();
    params.put("username", username);
    params.put("password", password);

    return new OAuth2PasswordAuthenticationToken(clientPrincipal, params);
  }
}

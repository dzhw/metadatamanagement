package eu.dzhw.fdz.metadatamanagement.usermanagement.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.resource.introspection.BadOpaqueTokenException;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.stereotype.Component;

@Component
public final class MongoOAuth2OpaqueTokenIntrospector implements OpaqueTokenIntrospector {

  private final OAuth2AuthorizationService authorizationService;
  private final UserDetailsService userDetailsService;

  public MongoOAuth2OpaqueTokenIntrospector(OAuth2AuthorizationService authorizationService,
      UserDetailsService userDetailsService) {
    this.authorizationService = authorizationService;
    this.userDetailsService = userDetailsService;
  }

  @Override
  public OAuth2AuthenticatedPrincipal introspect(String token) {
    OAuth2Authorization authorization = authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
    if (authorization == null) {
      throw new BadOpaqueTokenException("Invalid or expired access token");
    }
    OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getAccessToken();
    if (accessToken == null || !accessToken.isActive()) {
      throw new BadOpaqueTokenException("Invalid or expired access token");
    }

    UserDetails userDetails;
    try {
      userDetails = userDetailsService.loadUserByUsername(authorization.getPrincipalName());
    } catch (UsernameNotFoundException exception) {
      throw new BadOpaqueTokenException("Access token principal no longer exists");
    }

    List<GrantedAuthority> authorities = new ArrayList<>(userDetails.getAuthorities());
    authorization.getAuthorizedScopes().stream()
        .map(scope -> new SimpleGrantedAuthority("SCOPE_" + scope))
        .forEach(authorities::add);
    Map<String, Object> attributes = new HashMap<>();
    attributes.put("sub", userDetails.getUsername());
    attributes.put("scope", authorization.getAuthorizedScopes());
    return new DefaultOAuth2AuthenticatedPrincipal(userDetails.getUsername(), attributes, authorities);
  }
}
package eu.dzhw.fdz.metadatamanagement.usermanagement.security;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.stereotype.Component;

@Component
public final class PasswordGrantAuthenticationProvider implements AuthenticationProvider {

  private final AuthenticationManager authenticationManager;
  private final OAuth2AuthorizationService authorizationService;
  private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;

  public PasswordGrantAuthenticationProvider(AuthenticationManager authenticationManager,
      OAuth2AuthorizationService authorizationService,
      OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator) {
    this.authenticationManager = authenticationManager;
    this.authorizationService = authorizationService;
    this.tokenGenerator = tokenGenerator;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    PasswordGrantAuthenticationToken grantAuthentication =
        (PasswordGrantAuthenticationToken) authentication;
    if (!(grantAuthentication.getPrincipal() instanceof OAuth2ClientAuthenticationToken clientAuthentication)
        || !clientAuthentication.isAuthenticated()) {
      throw oauthError(OAuth2ErrorCodes.INVALID_CLIENT, "Client authentication is required");
    }

    var registeredClient = clientAuthentication.getRegisteredClient();
    if (registeredClient == null
        || !registeredClient.getAuthorizationGrantTypes().contains(PasswordGrantAuthenticationToken.GRANT_TYPE)) {
      throw oauthError(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT, "The client is not allowed to use this grant");
    }

    Set<String> authorizedScopes = grantAuthentication.getScopes().isEmpty()
        ? registeredClient.getScopes() : grantAuthentication.getScopes();
    if (!registeredClient.getScopes().containsAll(authorizedScopes)) {
      throw oauthError(OAuth2ErrorCodes.INVALID_SCOPE, "The requested scope is not allowed for this client");
    }

    Authentication userAuthentication;
    try {
      userAuthentication = authenticationManager.authenticate(
          UsernamePasswordAuthenticationToken.unauthenticated(
              grantAuthentication.getUsername(), grantAuthentication.getPassword()));
    } catch (AuthenticationException exception) {
      throw oauthError(OAuth2ErrorCodes.INVALID_GRANT, "Invalid resource owner credentials");
    }

    DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
        .registeredClient(registeredClient)
        .principal(userAuthentication)
        .authorizationServerContext(AuthorizationServerContextHolder.getContext())
        .authorizedScopes(authorizedScopes)
        .authorizationGrantType(PasswordGrantAuthenticationToken.GRANT_TYPE)
        .authorizationGrant(grantAuthentication);

    OAuth2Token generatedAccessToken = tokenGenerator.generate(tokenContextBuilder
        .tokenType(OAuth2TokenType.ACCESS_TOKEN).build());
    if (!(generatedAccessToken instanceof OAuth2AccessToken generatedAccessTokenValue)) {
      throw oauthError(OAuth2ErrorCodes.SERVER_ERROR, "Access token generation failed");
    }
    OAuth2AccessToken accessToken = new OAuth2AccessToken(generatedAccessTokenValue.getTokenType(),
      generatedAccessTokenValue.getTokenValue(), generatedAccessTokenValue.getIssuedAt(),
      generatedAccessTokenValue.getExpiresAt(), generatedAccessTokenValue.getScopes());

    OAuth2RefreshToken refreshToken = null;
    if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN)) {
      OAuth2Token generatedRefreshToken = tokenGenerator.generate(tokenContextBuilder
          .tokenType(OAuth2TokenType.REFRESH_TOKEN).build());
      if (!(generatedRefreshToken instanceof OAuth2RefreshToken)) {
        throw oauthError(OAuth2ErrorCodes.SERVER_ERROR, "Refresh token generation failed");
      }
      refreshToken = (OAuth2RefreshToken) generatedRefreshToken;
    }

    OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.withRegisteredClient(registeredClient)
        .principalName(userAuthentication.getName())
        .authorizationGrantType(PasswordGrantAuthenticationToken.GRANT_TYPE)
        .authorizedScopes(authorizedScopes)
        .attribute(Principal.class.getName(), userAuthentication)
        .accessToken(accessToken);
    if (refreshToken != null) {
      authorizationBuilder.refreshToken(refreshToken);
    }
    OAuth2Authorization authorization = authorizationBuilder.build();
    authorizationService.save(authorization);

    Map<String, Object> additionalParameters = new HashMap<>();
    additionalParameters.put(OAuth2ParameterNames.SCOPE,
        authorizedScopes.stream().sorted().collect(Collectors.joining(" ")));
    return new OAuth2AccessTokenAuthenticationToken(
        registeredClient, clientAuthentication, accessToken, refreshToken, additionalParameters);
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return PasswordGrantAuthenticationToken.class.isAssignableFrom(authentication);
  }

  private OAuth2AuthenticationException oauthError(String code, String description) {
    return new OAuth2AuthenticationException(new OAuth2Error(code, description, null));
  }
}
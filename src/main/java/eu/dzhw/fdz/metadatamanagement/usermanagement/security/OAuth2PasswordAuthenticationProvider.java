package eu.dzhw.fdz.metadatamanagement.usermanagement.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2AccessTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

//@Component
public class OAuth2PasswordAuthenticationProvider implements AuthenticationProvider {

  private final AuthenticationManager authenticationManager;
  private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;
//  private final OAuth2AuthorizationService authorizationService;
  private final RegisteredClient registeredClient;

  public OAuth2PasswordAuthenticationProvider(AuthenticationManager authenticationManager,
                                              OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
//                                              OAuth2AuthorizationService authorizationService
                                              RegisteredClient registeredClient) {
    this.authenticationManager = authenticationManager;
    this.tokenGenerator = tokenGenerator;
//    this.authorizationService = authorizationService;
    this.registeredClient = registeredClient;
  }

  OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator() {
    return context -> new OAuth2AccessToken(
      OAuth2AccessToken.TokenType.BEARER, "foo", Instant.now(), Instant.now(), Set.of("read", "write")
    );
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    final var passwordAuth = (OAuth2PasswordAuthenticationToken) authentication;
    final var userAuthentication = this.authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(passwordAuth.getUsername(), passwordAuth.getPassword()));

    final var authorization = OAuth2Authorization
      .withRegisteredClient(this.registeredClient)
      .principalName(userAuthentication.getName())
      .authorizationGrantType(new AuthorizationGrantType("password"))
      .build();

    final var accessToken = Optional.ofNullable(new OAuth2AccessTokenGenerator().generate(DefaultOAuth2TokenContext.builder()
      .registeredClient(this.registeredClient)
      .principal(userAuthentication)
      .authorization(authorization)
      .authorizationGrantType(new AuthorizationGrantType("password"))
      .tokenType(OAuth2TokenType.ACCESS_TOKEN)
      .build()
    )).orElseThrow();

    return new OAuth2AccessTokenAuthenticationToken(this.registeredClient, userAuthentication, accessToken);
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return OAuth2PasswordAuthenticationToken.class.isAssignableFrom(authentication);
  }
}

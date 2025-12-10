package eu.dzhw.fdz.metadatamanagement.usermanagement.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.OAuth2PasswordAuthenticationConverter;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.OAuth2PasswordAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import eu.dzhw.fdz.metadatamanagement.common.config.JHipsterProperties;
import eu.dzhw.fdz.metadatamanagement.usermanagement.repository.MongoDbTokenStore;
import eu.dzhw.fdz.metadatamanagement.usermanagement.repository.OAuth2AccessTokenRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.repository.OAuth2RefreshTokenRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AjaxLogoutSuccessHandler;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.Http401UnauthorizedEntryPoint;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

/**
 * Configure the ResourceServer and the AuthorizationServer.
 */
@Configuration
public class OAuth2ServerConfiguration {

  /**
   * Configure the ResourceServer.
   */
  @Configuration
  protected static class ResourceServerConfiguration {

    @Bean
    @Order(2)
    SecurityFilterChain resourceSecurityFilterChain(HttpSecurity http,
                                                    Http401UnauthorizedEntryPoint authenticationEntryPoint,
                                                    AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler,
                                                    AuthenticationManager authenticationManager,
                                                    JwtDecoder jwtDecoder)
      throws Exception
    {
      return http
        .exceptionHandling(exh ->
          exh.authenticationEntryPoint(authenticationEntryPoint)
        )

        .logout(logout -> logout
          .logoutUrl("/api/logout")
          .logoutSuccessHandler(ajaxLogoutSuccessHandler)
        )

        .csrf(csrf -> csrf
          .requireCsrfProtectionMatcher(new AntPathRequestMatcher("/oauth/authorize"))
          .ignoringRequestMatchers("/api/**", "/management/**")
          .disable()
        )

        .headers(headers ->
          headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
        )

        .authorizeHttpRequests(auth -> auth
          .requestMatchers("/oauth/token").permitAll()
          .requestMatchers(
            "/api/authenticate",
            "/api/register",
            "/api/orders/**",
            "/public/files/**",
            "/api/variables/**",
            "/api/surveys/**",
            "/api/i18n/**",
            "/api/instruments/**",
            "/api/data-sets/**",
            "/api/questions/**",
            "/api/data-packages/**",
            "/api/analysis-packages/**",
            "/api/concepts/**",
            "/api/study-serieses/**",
            "/api/related-publications/**",
            "/api/swagger-ui/**",
            "/api/swagger-ui.html",
            "/api/api-docs/**"
          ).permitAll()
          .requestMatchers(r ->
            r.getServletPath().startsWith("/api/data-acquisition-projects/") &&
              (
                r.getServletPath().endsWith("/releases") ||
                r.getServletPath().endsWith("/attachments")
              )
          ).permitAll()
          .requestMatchers("/api/**").authenticated()
          .requestMatchers(
            "/management/info",
            "/management/metrics",
            "/management/prometheus",
            "/management/health/**"
          ).permitAll()
          .requestMatchers("/management/**").hasAuthority("ROLE_ADMIN")
          .anyRequest().denyAll()
        )

        .oauth2ResourceServer(oauth2 -> oauth2
          .jwt(jwt -> jwt
            .authenticationManager(authenticationManager)
            .decoder(jwtDecoder)))

        .sessionManagement(session -> {
          session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
          session.sessionFixation().none();
        })

        .requiresChannel(channel -> channel
          .requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null)
          .requiresSecure()
        )

        .build();
    }
  }

  /**
   * Configure the AuthorizationServer.
   */
  @Configuration
  protected static class AuthorizationServerConfiguration {

    @Bean
    @Order(1)
    public SecurityFilterChain authSecurityFilterChain(HttpSecurity http,
                                                       JHipsterProperties jHipsterProperties,
                                                       OAuth2PasswordAuthenticationConverter passwordConverter,
                                                       RegisteredClientRepository registeredClientRepository,
                                                       AuthenticationManager authenticationManager)
      throws Exception
    {
      final var tokenGenerator = (OAuth2TokenGenerator<? extends OAuth2Token>) http.getSharedObject(OAuth2TokenGenerator.class);
      final var authServerConfigurer = OAuth2AuthorizationServerConfigurer.authorizationServer();
      final var registeredClient = registeredClientRepository.findByClientId(
        jHipsterProperties.getSecurity().getAuthentication().getOauth().getClientid());
      return http
        .securityMatcher(authServerConfigurer.getEndpointsMatcher())
        .with(authServerConfigurer, authServer ->
          authServer
            .tokenEndpoint(tokenEndpoint -> tokenEndpoint
              .accessTokenRequestConverter(passwordConverter)
              .authenticationProvider(
                new OAuth2PasswordAuthenticationProvider(authenticationManager, tokenGenerator, registeredClient)))
        )
        .build();
    }

    @Bean
    public MongoDbTokenStore tokenStore(OAuth2AccessTokenRepository oAuth2AccessTokenRepository,
                                        OAuth2RefreshTokenRepository oAuth2RefreshTokenRepository)
    {
      return new MongoDbTokenStore(
        oAuth2AccessTokenRepository,
        oAuth2RefreshTokenRepository,
        new UniqueAuthenticationKeyGenerator()
      );
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository(JHipsterProperties jHipsterProperties) {
      final var oidcClient = RegisteredClient.withId(UUID.randomUUID().toString())
        .clientId(jHipsterProperties.getSecurity().getAuthentication().getOauth().getClientid())
        .clientSecret(jHipsterProperties.getSecurity().getAuthentication().getOauth().getSecret())
        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
        .authorizationGrantType(new AuthorizationGrantType("password"))
        // .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
        .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
        .scope("read")
        .scope("write")
        .tokenSettings(TokenSettings.builder()
          .accessTokenFormat(OAuth2TokenFormat.REFERENCE)
          .build())
        .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
        .build();
      return new InMemoryRegisteredClientRepository(oidcClient);
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
      KeyPair keyPair = generateRsaKey();
      RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
      RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
      RSAKey rsaKey = new RSAKey.Builder(publicKey)
        .privateKey(privateKey)
        .keyID(UUID.randomUUID().toString())
        .build();
      JWKSet jwkSet = new JWKSet(rsaKey);
      return new ImmutableJWKSet<>(jwkSet);
    }

    private static KeyPair generateRsaKey() {
      KeyPair keyPair;
      try {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        keyPair = keyPairGenerator.generateKeyPair();
      }
      catch (Exception ex) {
        throw new IllegalStateException(ex);
      }
      return keyPair;
    }

    @Bean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
      return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
      return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
      return AuthorizationServerSettings.builder().build();
    }
  }
}

package eu.dzhw.fdz.metadatamanagement.usermanagement.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2AccessTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2RefreshTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AjaxLogoutSuccessHandler;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.Http401UnauthorizedEntryPoint;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.PasswordGrantAuthenticationConverter;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.PasswordGrantAuthenticationProvider;

/**
 * Configure the ResourceServer and the AuthorizationServer.
 */
@Configuration
public class OAuth2ServerConfiguration {

  @Autowired
  private Http401UnauthorizedEntryPoint authenticationEntryPoint;

  @Autowired
  private AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler;

  @Bean
  @Order(Ordered.HIGHEST_PRECEDENCE)
  SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http,
      PasswordGrantAuthenticationConverter passwordGrantAuthenticationConverter,
      PasswordGrantAuthenticationProvider passwordGrantAuthenticationProvider,
      OAuth2AuthorizationService authorizationService,
      OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
      AuthorizationServerSettings authorizationServerSettings) throws Exception {
    OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
        new OAuth2AuthorizationServerConfigurer();

    http.apply(authorizationServerConfigurer);
    authorizationServerConfigurer
        .authorizationServerSettings(authorizationServerSettings)
        .authorizationService(authorizationService)
        .tokenGenerator(tokenGenerator)
        .tokenEndpoint(tokenEndpoint -> tokenEndpoint
            .accessTokenRequestConverter(passwordGrantAuthenticationConverter)
            .authenticationProvider(passwordGrantAuthenticationProvider));

    http.securityMatcher(authorizationServerConfigurer.getEndpointsMatcher());
    http.csrf(csrf -> csrf.ignoringRequestMatchers(authorizationServerConfigurer.getEndpointsMatcher()));
    http.authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated());
    return http.build();
  }

  @Bean
  AuthorizationServerSettings authorizationServerSettings() {
    return AuthorizationServerSettings.builder()
        .tokenEndpoint("/oauth/token")
        .build();
  }

  @Bean
  OAuth2TokenGenerator<? extends OAuth2Token> oauth2TokenGenerator() {
    return new DelegatingOAuth2TokenGenerator(
        new OAuth2AccessTokenGenerator(), new OAuth2RefreshTokenGenerator());
  }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http,
            OpaqueTokenIntrospector opaqueTokenIntrospector) throws Exception {
    http
        .exceptionHandling(ex -> ex
            .authenticationEntryPoint(authenticationEntryPoint))
        .logout(logout -> logout
            .logoutUrl("/api/logout")
            .logoutSuccessHandler(ajaxLogoutSuccessHandler))
        .csrf(csrf -> csrf
            .requireCsrfProtectionMatcher(new AntPathRequestMatcher("/oauth/authorize"))
            .ignoringRequestMatchers("/api/**", "/management/**"))
        .headers(headers -> headers
            .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/authenticate", "/api/register").permitAll()
            .requestMatchers("/api/orders/**", "/public/files/**", "/api/variables/**", "/api/surveys/**",
                "/api/i18n/**", "/api/instruments/**", "/api/data-sets/**", "/api/questions/**",
                "/api/data-packages/**", "/api/analysis-packages/**", "/api/concepts/**",
                "/api/study-serieses/**", "/api/related-publications/**",
                "/api/swagger-ui/**",
                "/api/swagger-ui.html", "/api/api-docs/**")
            .permitAll()
            .requestMatchers(
                AntPathRequestMatcher.antMatcher("/api/data-acquisition-projects/**/releases"),
                AntPathRequestMatcher.antMatcher("/api/data-acquisition-projects/**/attachments"))
                .permitAll()
            .requestMatchers("/api/**").authenticated()
            .requestMatchers("/management/info", "/management/metrics",
                "/management/prometheus", "/management/health/**")
            .permitAll()
            .requestMatchers("/management/**").hasAuthority("ROLE_ADMIN"))
        .httpBasic(Customizer.withDefaults())
        .oauth2ResourceServer(oauth -> oauth
            .opaqueToken(opaque -> opaque.introspector(opaqueTokenIntrospector)))
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .sessionFixation().none())
        .requiresChannel(channel -> channel
            .requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null)
            .requiresSecure());
    return http.build();
  }
}

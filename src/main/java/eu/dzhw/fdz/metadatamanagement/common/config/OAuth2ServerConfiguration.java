package eu.dzhw.fdz.metadatamanagement.common.config;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import eu.dzhw.fdz.metadatamanagement.common.config.oauth2.MongoDbTokenStore;
import eu.dzhw.fdz.metadatamanagement.repository.OAuth2AccessTokenRepository;
import eu.dzhw.fdz.metadatamanagement.repository.OAuth2RefreshTokenRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AjaxLogoutSuccessHandler;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.Http401UnauthorizedEntryPoint;

/**
 * Configure the ResourceServer and the AuthorizationServer.
 */
@Configuration
public class OAuth2ServerConfiguration {

  /**
   * Configure the ResourceServer.
   */
  @Configuration
  @EnableResourceServer
  protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Inject
    private Http401UnauthorizedEntryPoint authenticationEntryPoint;

    @Inject
    private AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler;

    @Inject
    private Environment environment;

    @Override
    public void configure(HttpSecurity http) throws Exception {
      http.exceptionHandling()
        .authenticationEntryPoint(authenticationEntryPoint)
        .and()
        .logout()
        .logoutUrl("/api/logout")
        .logoutSuccessHandler(ajaxLogoutSuccessHandler)
        .and()
        .csrf()
        .requireCsrfProtectionMatcher(new AntPathRequestMatcher("/oauth/authorize"))
        .disable()
        .headers()
        .frameOptions()
        .disable()
        .and()
        // disable csrf protection for api and management endpoints
        .csrf()
        .ignoringAntMatchers("/api/**", "/management/**", "/api-docs/**")
        .and()
        .authorizeRequests()
        .antMatchers("/api/authenticate")
        .permitAll()
        .antMatchers("/api/register")
        .permitAll()
        .antMatchers("/api/logs/**")
        .hasAnyAuthority(AuthoritiesConstants.ADMIN)
        .antMatchers("/api/**")
        .authenticated()
        .antMatchers("/metrics/**")
        .hasAuthority(AuthoritiesConstants.ADMIN)
        .antMatchers("/health/**")
        .hasAuthority(AuthoritiesConstants.ADMIN)
        .antMatchers("/trace/**")
        .hasAuthority(AuthoritiesConstants.ADMIN)
        .antMatchers("/dump/**")
        .hasAuthority(AuthoritiesConstants.ADMIN)
        .antMatchers("/shutdown/**")
        .hasAuthority(AuthoritiesConstants.ADMIN)
        .antMatchers("/beans/**")
        .hasAuthority(AuthoritiesConstants.ADMIN)
        .antMatchers("/configprops/**")
        .hasAuthority(AuthoritiesConstants.ADMIN)
        .antMatchers("/info/**")
        .hasAuthority(AuthoritiesConstants.ADMIN)
        .antMatchers("/autoconfig/**")
        .hasAuthority(AuthoritiesConstants.ADMIN)
        .antMatchers("/env/**")
        .hasAuthority(AuthoritiesConstants.ADMIN)
        .antMatchers("/trace/**")
        .hasAuthority(AuthoritiesConstants.ADMIN)
        .antMatchers("/liquibase/**")
        .hasAuthority(AuthoritiesConstants.ADMIN)
        .antMatchers("/api-docs/**")
        .hasAuthority(AuthoritiesConstants.ADMIN)
        .antMatchers("/protected/**")
        .authenticated()
        // enable basic http for /api and spring endpoints and /api-docs
        .and()
        .authorizeRequests()
        .antMatchers("/api/**", "/api-docs/**")
        .hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.USER)
        .and()
        .httpBasic()
        .and()
        .authorizeRequests()
        .antMatchers("/management/**")
        .hasAuthority(AuthoritiesConstants.ADMIN)
        .and()
        .httpBasic();

      // Enforce HTTPS except on dev
      if (environment.acceptsProfiles("!dev")) {
        http.requiresChannel()
          .anyRequest()
          .requiresSecure();
      }

    }
  }

  /**
   * Configure the AuthorizationServer.
   */
  @Configuration
  @EnableAuthorizationServer
  protected static class AuthorizationServerConfiguration
      extends AuthorizationServerConfigurerAdapter {

    @Inject
    private OAuth2AccessTokenRepository oauth2AccessTokenRepository;

    @Inject
    private OAuth2RefreshTokenRepository oauth2RefreshTokenRepository;

    @Inject
    private JHipsterProperties jhipsterProperties;

    @Bean
    public TokenStore tokenStore() {
      return new MongoDbTokenStore(oauth2AccessTokenRepository, oauth2RefreshTokenRepository);
    }

    @Inject
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

      endpoints.tokenStore(tokenStore())
        .authenticationManager(authenticationManager);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
      clients.inMemory()
          .withClient(jhipsterProperties.getSecurity()
          .getAuthentication()
          .getOauth()
          .getClientid())
          .scopes("read", "write")
          .authorities(AuthoritiesConstants.ADMIN, AuthoritiesConstants.USER)
          .authorizedGrantTypes("password", "refresh_token")
          .secret(jhipsterProperties.getSecurity()
          .getAuthentication()
          .getOauth()
          .getSecret())
          .accessTokenValiditySeconds(jhipsterProperties.getSecurity()
          .getAuthentication()
          .getOauth()
          .getTokenValidityInSeconds());
    }
  }
}

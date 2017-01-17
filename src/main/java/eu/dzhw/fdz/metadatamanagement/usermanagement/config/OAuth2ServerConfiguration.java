package eu.dzhw.fdz.metadatamanagement.usermanagement.config;

import org.springframework.beans.factory.annotation.Autowired;
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

import eu.dzhw.fdz.metadatamanagement.common.config.Constants;
import eu.dzhw.fdz.metadatamanagement.common.config.JHipsterProperties;
import eu.dzhw.fdz.metadatamanagement.usermanagement.repository.MongoDbTokenStore;
import eu.dzhw.fdz.metadatamanagement.usermanagement.repository.OAuth2AccessTokenRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.repository.OAuth2RefreshTokenRepository;
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

    @Autowired
    private Http401UnauthorizedEntryPoint authenticationEntryPoint;

    @Autowired
    private AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler;

    @Autowired
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
        .ignoringAntMatchers("/api/**", "/management/**")
        .and()
        .authorizeRequests()
        .antMatchers("/api/authenticate")
        .permitAll()
        .antMatchers("/api/register")
        .permitAll()
        // enable basic http for /api and spring endpoints
        .and()
        .authorizeRequests()
        .antMatchers("/api/**")
        .authenticated()
        .and()
        .httpBasic()
        .and()
        .authorizeRequests()
        .antMatchers("/management/**")
        .hasAuthority(AuthoritiesConstants.ADMIN)
        .and()
        .httpBasic();

      // Enforce HTTPS except on local machine
      if (environment.acceptsProfiles("!" + Constants.SPRING_PROFILE_LOCAL)) {
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

    @Autowired
    private OAuth2AccessTokenRepository oauth2AccessTokenRepository;

    @Autowired
    private OAuth2RefreshTokenRepository oauth2RefreshTokenRepository;

    @Autowired
    private JHipsterProperties jhipsterProperties;

    @Bean
    public TokenStore tokenStore() {
      return new MongoDbTokenStore(oauth2AccessTokenRepository, oauth2RefreshTokenRepository);
    }

    @Autowired
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
          .authorities(AuthoritiesConstants.ADMIN, AuthoritiesConstants.USER, 
              AuthoritiesConstants.PUBLISHER)
          .authorizedGrantTypes("password", "refresh_token")
          .secret(jhipsterProperties.getSecurity()
          .getAuthentication()
          .getOauth()
          .getSecret()).accessTokenValiditySeconds(0);
    }
  }
}

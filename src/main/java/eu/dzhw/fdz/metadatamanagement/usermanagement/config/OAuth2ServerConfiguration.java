package eu.dzhw.fdz.metadatamanagement.usermanagement.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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

    @Override
    public void configure(HttpSecurity http) throws Exception {
      http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).and().logout()
          .logoutUrl("/api/logout").logoutSuccessHandler(ajaxLogoutSuccessHandler).and().csrf()
          .requireCsrfProtectionMatcher(new AntPathRequestMatcher("/oauth/authorize")).disable()
          .headers().frameOptions().disable().and()
          // disable csrf protection for api
          .csrf().ignoringAntMatchers("/api/**", "/management/**").and().authorizeRequests()
          .antMatchers("/api/authenticate").permitAll().antMatchers("/api/register").permitAll()
          .antMatchers("/api/orders/**", "/public/files/**", "/api/variables/**", "/api/surveys/**",
              "/api/i18n/**", "/api/instruments/**", "/api/data-sets/**", "/api/questions/**",
              "/api/studies/**", "/api/concepts/**", "/api/study-serieses/**",
              "/api/related-publications/**", "/api/data-acquisition-projects/**/releases",
              "/api/data-acquisition-projects/**/attachments", "/api/swagger-ui.html",
              "/api/swagger-ui/**", "/api/api-docs")
          .permitAll()
          // enable basic http for /api
          .and().authorizeRequests().antMatchers("/api/**").authenticated().and().httpBasic().and()
          .authorizeRequests().antMatchers("/management/info").permitAll().and().authorizeRequests()
          .antMatchers("/management/metrics").permitAll().and().authorizeRequests()
          .antMatchers("/management/prometheus").permitAll().antMatchers("/management/health/**")
          .permitAll().antMatchers("/management/**").hasAuthority("ROLE_ADMIN").and()
          .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
          .enableSessionUrlRewriting(false);

      // Enforce HTTPS when the request comes through a proxy/load balancer
      http.requiresChannel().requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null)
          .requiresSecure();
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
    public MongoDbTokenStore tokenStore() {
      return new MongoDbTokenStore(oauth2AccessTokenRepository, oauth2RefreshTokenRepository,
          new UniqueAuthenticationKeyGenerator());
    }

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

      endpoints.tokenStore(tokenStore()).authenticationManager(authenticationManager);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
      clients.inMemory()
          .withClient(jhipsterProperties.getSecurity().getAuthentication().getOauth().getClientid())
          .scopes("read", "write")
          .authorities(AuthoritiesConstants.ADMIN, AuthoritiesConstants.USER,
              AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER)
          .authorizedGrantTypes("password", "refresh_token")
          .secret(jhipsterProperties.getSecurity().getAuthentication().getOauth().getSecret())
          .accessTokenValiditySeconds(0);
    }
  }
}

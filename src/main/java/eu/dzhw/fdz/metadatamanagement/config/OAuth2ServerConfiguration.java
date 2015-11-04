package eu.dzhw.fdz.metadatamanagement.config;

import javax.inject.Inject;
import javax.sql.DataSource;

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
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import eu.dzhw.fdz.metadatamanagement.security.AjaxLogoutSuccessHandler;
import eu.dzhw.fdz.metadatamanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.security.Http401UnauthorizedEntryPoint;

@Configuration
public class OAuth2ServerConfiguration {

  @Configuration
  @EnableResourceServer
  protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Inject
    private Http401UnauthorizedEntryPoint authenticationEntryPoint;

    @Inject
    private AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler;

    @Inject
    private Environment env;

    @Override
    public void configure(HttpSecurity http) throws Exception {
      http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).and().logout()
          .logoutUrl("/api/logout").logoutSuccessHandler(ajaxLogoutSuccessHandler).and().csrf()
          .requireCsrfProtectionMatcher(new AntPathRequestMatcher("/oauth/authorize")).disable()
          .headers().frameOptions().disable().and().csrf()
          .ignoringAntMatchers("/api/**", "/management/**", "/api-docs/**").and()
          .authorizeRequests().antMatchers("/api/authenticate").permitAll()
          .antMatchers("/api/register").permitAll().antMatchers("/api/logs/**")
          .hasAnyAuthority(AuthoritiesConstants.ADMIN).antMatchers("/api/**").authenticated()
          .antMatchers("/websocket/tracker").hasAuthority(AuthoritiesConstants.ADMIN)
          .antMatchers("/websocket/**").permitAll().antMatchers("/management/**")
          .hasAuthority(AuthoritiesConstants.ADMIN).antMatchers("/api-docs/**")
          .hasAuthority(AuthoritiesConstants.ADMIN).antMatchers("/protected/**").authenticated()
          // enable basic http for /api and spring endpoints and /api-docs
          .and().authorizeRequests().antMatchers("/api/**", "/api-docs/**")
          .hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.USER).and().httpBasic()
          .and().authorizeRequests().antMatchers("/management/**")
          .hasAuthority(AuthoritiesConstants.ADMIN).and().httpBasic();
      // Enforce HTTPS except on dev
      if (env.acceptsProfiles("!dev")) {
        http.requiresChannel().anyRequest().requiresSecure();
      }
    }
  }

  @Configuration
  @EnableAuthorizationServer
  protected static class AuthorizationServerConfiguration
      extends AuthorizationServerConfigurerAdapter {

    @Inject
    private DataSource dataSource;

    @Inject
    private JHipsterProperties jHipsterProperties;

    @Bean
    public TokenStore tokenStore() {
      return new JdbcTokenStore(dataSource);
    }

    @Inject
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

      endpoints.tokenStore(tokenStore()).authenticationManager(authenticationManager);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
      clients.inMemory()
          .withClient(jHipsterProperties.getSecurity().getAuthentication().getOauth().getClientid())
          .scopes("read", "write")
          .authorities(AuthoritiesConstants.ADMIN, AuthoritiesConstants.USER)
          .authorizedGrantTypes("password", "refresh_token")
          .secret(jHipsterProperties.getSecurity().getAuthentication().getOauth().getSecret())
          .accessTokenValiditySeconds(jHipsterProperties.getSecurity().getAuthentication()
              .getOauth().getTokenValidityInSeconds());
    }
  }
}

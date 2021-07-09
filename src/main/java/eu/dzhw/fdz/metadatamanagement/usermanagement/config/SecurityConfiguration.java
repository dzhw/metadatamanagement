package eu.dzhw.fdz.metadatamanagement.usermanagement.config;

import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticatedActionsFilter;
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter;
import org.keycloak.adapters.springsecurity.filter.KeycloakPreAuthActionsFilter;
import org.keycloak.adapters.springsecurity.filter.KeycloakSecurityContextRequestFilter;
import org.keycloak.adapters.springsecurity.management.HttpSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

/**
 * Configure password encryption.
 */
@KeycloakConfiguration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration extends KeycloakWebSecurityConfigurerAdapter {

  @Bean
  public KeycloakSpringBootConfigResolver KeycloakConfigResolver() {
    return new KeycloakSpringBootConfigResolver();
  }

  @Override
  public void configure(WebSecurity web) {
    web.ignoring().antMatchers("/scripts/**/*.{js,html}").antMatchers("/node_modules/**")
        .antMatchers("/websocket/**").antMatchers("/i18n/**").antMatchers("/assets/**")
        .antMatchers("/api/register").antMatchers("/api/activate")
        .antMatchers("/api/account/reset-password/init")
        .antMatchers("/api/account/reset-password/finish")
        .antMatchers(HttpMethod.POST, "/api/search/**/_search")
        .antMatchers(HttpMethod.GET, "/api/search/**")
        .antMatchers(HttpMethod.POST, "/api/search/**/_mget")
        .antMatchers(HttpMethod.POST, "/api/search/**/_count")
        .antMatchers(HttpMethod.GET, "/management/info");
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    super.configure(http);

    http
        // disable csrf protection for api
        .csrf().ignoringAntMatchers("/api/**", "/management/**").and().authorizeRequests()
        .antMatchers("/api/authenticate").permitAll().antMatchers("/api/register").permitAll()
        .antMatchers("/api/orders/**", "/public/files/**", "/api/variables/**", "/api/surveys/**",
            "/api/i18n/**", "/api/instruments/**", "/api/data-sets/**", "/api/questions/**",
            "/api/data-packages/**", "/api/concepts/**", "/api/study-serieses/**",
            "/api/related-publications/**", "/api/data-acquisition-projects/**/releases",
            "/api/data-acquisition-projects/**/attachments", "/api/swagger-ui/**",
            "/api/swagger-ui.html", "/api/api-docs/**")
        .permitAll();
    // enable basic http for /api
    // .and().authorizeRequests().antMatchers("/api/**").authenticated().and().httpBasic().and()
    // .authorizeRequests().antMatchers("/management/info").permitAll().and().authorizeRequests()
    // .antMatchers("/management/metrics").permitAll().and().authorizeRequests()
    // .antMatchers("/management/prometheus").permitAll().antMatchers("/management/health/**")
    // .permitAll().antMatchers("/management/**").hasAuthority("ROLE_ADMIN").and()
    // .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    // .enableSessionUrlRewriting(false);
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .sessionAuthenticationStrategy(sessionAuthenticationStrategy()).and().headers()
        .frameOptions().sameOrigin();
    // Enforce HTTPS when the request comes through a proxy/load balancer
    http.requiresChannel().requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null)
        .requiresSecure();
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) {
    auth.authenticationProvider(keycloakAuthenticationProvider());
  }

  @Bean
  public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
    return new SecurityEvaluationContextExtension();
  }

  @Bean
  @Override
  protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
    return new NullAuthenticatedSessionStrategy();
  }

  @Bean
  public FilterRegistrationBean keycloakAuthenticationProcessingFilterRegistrationBean(
      KeycloakAuthenticationProcessingFilter filter) {
    FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
    registrationBean.setEnabled(false);
    return registrationBean;
  }

  @Bean
  public FilterRegistrationBean keycloakPreAuthActionsFilterRegistrationBean(
      KeycloakPreAuthActionsFilter filter) {
    FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
    registrationBean.setEnabled(false);
    return registrationBean;
  }

  @Bean
  public FilterRegistrationBean keycloakAuthenticatedActionsFilterBean(
      KeycloakAuthenticatedActionsFilter filter) {
    FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
    registrationBean.setEnabled(false);
    return registrationBean;
  }

  @Bean
  public FilterRegistrationBean keycloakSecurityContextRequestFilterBean(
      KeycloakSecurityContextRequestFilter filter) {
    FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
    registrationBean.setEnabled(false);
    return registrationBean;
  }

  @Bean
  @Override
  @ConditionalOnMissingBean(HttpSessionManager.class)
  protected HttpSessionManager httpSessionManager() {
    return new HttpSessionManager();
  }
}

package eu.dzhw.fdz.metadatamanagement.usermanagement.config;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;

/**
 * Configure password encryption.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Inject
  private UserDetailsService userDetailsService;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Inject
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService)
      .passwordEncoder(passwordEncoder());
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring()
      .antMatchers("/scripts/**/*.{js,html}")
      .antMatchers("/bower_components/**")
      .antMatchers("/i18n/**")
      .antMatchers("/assets/**")
      .antMatchers("/api/register")
      .antMatchers("/api/activate")
      .antMatchers("/api/account/reset-password/init")
      .antMatchers("/api/account/reset-password/finish")
      .antMatchers("/test/**")
      .antMatchers(HttpMethod.GET, "/public/files/**")
      .antMatchers(HttpMethod.GET, "/api/variables/**")
      .antMatchers(HttpMethod.GET, "/api/surveys/**")
      .antMatchers(HttpMethod.GET, "/api/data-sets/**")
      .antMatchers(HttpMethod.GET, "/api/questions/**")
      .antMatchers(HttpMethod.GET, "/api/studies/**")
      .antMatchers(HttpMethod.GET, "/api/related-publications/**")
      .antMatchers(HttpMethod.POST, "/api/search/**/_search")
      .antMatchers(HttpMethod.POST, "/api/search/**/_mget")
      .antMatchers(HttpMethod.POST, "/api/search/**/_count");
  }

  @Override
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
    return new SecurityEvaluationContextExtension();
  }
}

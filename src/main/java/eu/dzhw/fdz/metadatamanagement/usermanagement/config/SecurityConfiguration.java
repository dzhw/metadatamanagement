package eu.dzhw.fdz.metadatamanagement.usermanagement.config;

import static eu.dzhw.fdz.metadatamanagement.usermanagement.config.AntStylePathRequestMatcher.antPath;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;

/**
 * Web and method security configuration.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration {

  @Autowired
  private UserDetailsService userDetailsService;

  @Bean
  public AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder) {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder);
    return new org.springframework.security.authentication.ProviderManager(provider);
  }

  /**
   * Requests which bypass the security filter chains completely. This is intentionally kept as
   * {@code web.ignoring()} (instead of {@code permitAll()}) so that anonymous search requests with
   * an expired bearer token are not rejected by the resource server.
   */
  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return web -> web.ignoring()
        .requestMatchers("/node_modules/**", "/websocket/**",
            "/i18n/**", "/assets/**", "/api/register", "/api/activate",
            "/api/account/reset-password/init", "/api/account/reset-password/finish",
            "/management/info")
        .requestMatchers(antPath("/scripts/**/*.js"), antPath("/scripts/**/*.html"),
            antPath(HttpMethod.POST, "/api/search/**/_search"),
            antPath(HttpMethod.POST, "/api/search/**/_mget"),
            antPath(HttpMethod.POST, "/api/search/**/_count"),
            antPath(HttpMethod.POST, "/api/search/**/_msearch"))
        .requestMatchers(HttpMethod.GET, "/api/search/**");
  }

  @Bean
  public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
    return new SecurityEvaluationContextExtension();
  }

}

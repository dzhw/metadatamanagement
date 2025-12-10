package eu.dzhw.fdz.metadatamanagement.usermanagement.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;

/**
 * Configure password encryption.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@AllArgsConstructor
public class SecurityConfiguration {

  private final UserDetailsService userDetailsService;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager() throws Exception {
    var provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(this.userDetailsService);
    provider.setPasswordEncoder(this.passwordEncoder());
    return new ProviderManager(provider);
  }

  // TODO consolidate with security filter chain config in OAuth2ServerConfiguration
  // ignoring requestMatchers is deprecated
  @Bean
  WebSecurityCustomizer webSecurityCustomizer() {
    return web -> web
      .ignoring()
        .requestMatchers(r ->
          r.getMethod().equals(HttpMethod.GET.toString()) &&
          r.getServletPath().startsWith("/scripts/") &&
          r.getServletPath().endsWith(".js") &&
          r.getServletPath().contains(".html")
        )
        .requestMatchers("/node_modules/**")
        .requestMatchers("/websocket/**")
        .requestMatchers("/i18n/**")
        .requestMatchers("/assets/**")
        .requestMatchers("/api/register")
        .requestMatchers("/api/activate")
        .requestMatchers("/api/account/reset-password/init")
        .requestMatchers("/api/account/reset-password/finish")
        .requestMatchers(r ->
          r.getMethod().equals(HttpMethod.POST.toString()) &&
          r.getServletPath().startsWith("/api/search/") &&
          r.getServletPath().endsWith("/_search")
        )
        .requestMatchers(HttpMethod.GET, "/api/search/**")
        .requestMatchers(r ->
          r.getMethod().equals(HttpMethod.POST.toString()) &&
          r.getServletPath().startsWith("/api/search/") &&
          (
            r.getServletPath().endsWith("/_mget") ||
            r.getServletPath().endsWith("/_count") ||
            r.getServletPath().endsWith("/_msearch")
          )
        )
        .requestMatchers(HttpMethod.GET, "/management/info");
  }

  @Bean
  public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
    return new SecurityEvaluationContextExtension();
  }
}

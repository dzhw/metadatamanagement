package eu.dzhw.fdz.metadatamanagement.usermanagement.config;

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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration {

  @Autowired
  private UserDetailsService userDetailsService;

  @Bean
  public AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder) {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder);
    return new org.springframework.security.authentication.ProviderManager(provider);
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return web -> web.ignoring()
        .requestMatchers("/node_modules/**", "/websocket/**",
            "/i18n/**", "/assets/**", "/api/register", "/api/activate",
            "/api/account/reset-password/init", "/api/account/reset-password/finish",
            "/management/info")
        .requestMatchers(AntPathRequestMatcher.antMatcher("/scripts/**/*.js"))
        .requestMatchers(AntPathRequestMatcher.antMatcher("/scripts/**/*.html"))
        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/search/**/_search"))
        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/search/**/_mget"))
        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/search/**/_count"))
        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/search/**/_msearch"))
        .requestMatchers(HttpMethod.GET, "/api/search/**");
  }

  @Bean
  public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
    return new SecurityEvaluationContextExtension();
  }

}

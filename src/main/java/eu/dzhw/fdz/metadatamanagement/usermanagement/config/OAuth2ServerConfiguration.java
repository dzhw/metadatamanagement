package eu.dzhw.fdz.metadatamanagement.usermanagement.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AjaxLogoutSuccessHandler;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.Http401UnauthorizedEntryPoint;

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
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {    http
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
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .sessionFixation().none())
        .requiresChannel(channel -> channel
            .requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null)
            .requiresSecure());
    return http.build();
  }
}

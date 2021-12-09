package eu.dzhw.fdz.metadatamanagement.authmanagement.config;

import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.Http401UnauthorizedEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

/**
 * All configurations related to web security and OAuth2 Resource Server settings.
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class Oauth2ResourceServerConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired
  private Http401UnauthorizedEntryPoint unauthorizedEntryPoint;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .requiresChannel()
          .requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null)
          .requiresSecure()
      .and()
          .sessionManagement()
          .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
          .enableSessionUrlRewriting(false)
      .and().headers().frameOptions().disable()
      .and().exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint)
      .and().csrf().ignoringAntMatchers(
          "/api/**",
          "/management/**"
      )
      .and().authorizeRequests(
          authorize ->
              authorize
              .mvcMatchers(
                "/api/orders/**",
                "/public/files/**",
                "/api/variables/**",
                "/api/surveys/**",
                "/api/i18n/**",
                "/api/instruments/**",
                "/api/data-packages/**",
                "/api/analysis-packages/**",
                "/api/concepts/**",
                "/api/study-serieses/**",
                "/api/related-publications/**",
                "/api/data-acquisition-projects/**/releases",
                "/api/data-acquisition-projects/**/attachments",
                "/api/swagger-ui/**",
                "/api/swagger-ui.html",
                "/api/api-docs/**"
              ).permitAll()
              .mvcMatchers(
                "/management/info",
                "/management/metrics",
                "/management/prometheus",
                "/management/health/**"
              ).permitAll()
              .mvcMatchers("/api/**").authenticated()
              .mvcMatchers("/management/**").hasAuthority(AuthoritiesConstants.ADMIN)
      )
      .oauth2ResourceServer()
      .jwt();
  }

  @Override
  public void configure(WebSecurity web) {
    web.ignoring()
        .mvcMatchers(
            "/scripts/**/*.{js,html}",
            "/node_modules/**",
            "/websocket/**",
            "/i18n/**",
            "/assets/**"
        )
        .mvcMatchers(
            HttpMethod.GET,
            "/management/info",
            "/api/search/**"
        )
        .mvcMatchers(
            HttpMethod.POST,
            "/api/search/**/_search",
            "/api/search/**/_mget",
            "/api/search/**/_count",
            "/api/search/**/_msearch"
      );
  }

  @Bean
  public JwtDecoder jwtDecoder(
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") final String issuerURI
  ) {
    var decoder = (NimbusJwtDecoder) JwtDecoders.fromIssuerLocation(issuerURI);

    decoder.setJwtValidator(new JwtTimestampValidator());

    return decoder;
  }
}

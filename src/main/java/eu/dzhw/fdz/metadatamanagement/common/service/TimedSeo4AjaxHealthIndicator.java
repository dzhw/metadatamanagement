package eu.dzhw.fdz.metadatamanagement.common.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import eu.dzhw.fdz.metadatamanagement.common.config.Constants;
import io.micrometer.core.annotation.Timed;

/**
 * {@link HealthIndicator} for our prerender service measuring the response times.
 * 
 * @author René Reitmann
 */
@Component("seo4AjaxHealthIndicator")
@Profile(Constants.SPRING_PROFILE_PROD)
public class TimedSeo4AjaxHealthIndicator extends AbstractHealthIndicator {
  private RestTemplate restTemplate;

  @Value("${metadatamanagement.seo4ajax.site-token}")
  private String siteToken;

  @Value("${metadatamanagement.seo4ajax.api-url:https://api.seo4ajax.com/}")
  private String apiUrl;
  
  public TimedSeo4AjaxHealthIndicator() {
    super();
    restTemplate = new RestTemplate();
  }

  @Override
  @Timed("seo_health_check")
  protected void doHealthCheck(Health.Builder builder) throws Exception {
    builder.withDetail("url", apiUrl + siteToken + "/status");
    ResponseEntity<String> response =
        restTemplate.getForEntity(apiUrl + siteToken + "/", String.class);
    if (response.getStatusCode().is2xxSuccessful()) {
      builder.up();
    } else {
      builder.withDetail("status", response.getStatusCode());
      builder.down();
    }
  }
}

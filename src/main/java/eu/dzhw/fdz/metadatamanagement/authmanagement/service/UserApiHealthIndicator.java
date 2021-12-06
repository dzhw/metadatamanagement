package eu.dzhw.fdz.metadatamanagement.authmanagement.service;

import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

/**
 * A Health indicator which checks to see if the server(s) providing the User API are up and
 * healthy.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class UserApiHealthIndicator extends AbstractHealthIndicator {

  private final UserApiService userApiService;

  @Override
  @Timed("user_api_health_check")
  protected void doHealthCheck(Health.Builder builder) throws Exception {
    try {
      if (this.userApiService.isHealthy()) {
        builder.up();
        builder.withDetail("location", this.userApiService.authServerEndpoint);
      } else {
        builder.down();
      }
    } catch (Exception e) {
      log.error("User API health check failed:", e);
      builder.down(e);
    }
  }
}

package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.stereotype.Component;

/**
 * A health indicator implementation for the da|ra PID service.
 * This indicator automatically exposes an endpoint at <code>/management/health/daraPid</code>
 * and is included with the health summary at <code>/management/health</code>. The naming is
 * inferred from the class name.
 * @see <a href="https://docs.spring.io/spring-boot/reference/actuator/endpoints.html#actuator.endpoints.health">Spring Boot Health Endpoints</a>
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class DaraPidHealthIndicator extends AbstractHealthIndicator {

  private final DaraPidClientService daraPidClientService;

  @Override
  @Timed("dara_pid_health_check")
  protected void doHealthCheck(Builder builder) throws Exception {
    if (this.daraPidClientService.serviceIsReachable()) {
      builder.up();
      builder.withDetail("location", this.daraPidClientService.getRegistationEndpoint());
    } else {
      builder.down();
    }
  }
}

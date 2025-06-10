package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataCiteHealthIndicator extends AbstractHealthIndicator {

  private final DataCiteService dataCiteService;

  @Override
  @Timed("datacite_health_check")
  protected void doHealthCheck(Health.Builder builder) throws Exception {
    try {
      //check dara health
      if (this.dataCiteService.isDataCiteHealthy()) {
        builder.up();
        builder.withDetail("location", this.dataCiteService.getApiEndpoint());
      } else {
        builder.down();
      }
    } catch (Exception e) {
      log.error("DataCite health check failed:", e);
      builder.down(e);
    }
  }
}

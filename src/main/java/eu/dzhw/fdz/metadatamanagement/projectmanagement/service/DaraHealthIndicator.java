package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.stereotype.Component;

import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Health Indicator for Dara.
 * 
 * @author Daniel Katzberg
 *
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class DaraHealthIndicator extends AbstractHealthIndicator {
  
  private final DaraService daraService;

  /*
   * (non-Javadoc)
   * @see org.springframework.boot.actuate.health
   * .AbstractHealthIndicator#doHealthCheck(org.springframework.boot.actuate.health.Health.Builder)
   */
  @Override
  @Timed("dara_health_check")
  protected void doHealthCheck(Builder builder) throws Exception {
    try {      
      //check dara health
      if (this.daraService.isDaraHealthy()) {
        builder.up();
        builder.withDetail("location", this.daraService.getApiEndpoint());
      } else {
        builder.down();
      }
    } catch (Exception e) {
      log.error("Dara health check failed:", e);
      builder.down(e);
    }
  }

}

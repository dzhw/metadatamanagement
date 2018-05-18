package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.stereotype.Component;

import com.codahale.metrics.annotation.Timed;

import lombok.extern.slf4j.Slf4j;

/**
 * Health Indicator for Dara.
 * 
 * @author Daniel Katzberg
 *
 */
@Component
@Slf4j
public class DaraHealthIndicator extends AbstractHealthIndicator {
  
  private DaraService daraService;
  
  /**
   * The health check is external and needs a dara rest resource for checking health. 
   * 
   * @param daraService The rest resource for calling the rest api of dara.
   */
  @Autowired
  public DaraHealthIndicator(DaraService daraService) {
    this.daraService = daraService;
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.boot.actuate.health
   * .AbstractHealthIndicator#doHealthCheck(org.springframework.boot.actuate.health.Health.Builder)
   */
  @Override
  @Timed
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

package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health.Builder;

/**
 * Health Indicator for Dara.
 * 
 * @author Daniel Katzberg
 *
 */
public class DaraHealthIndicator extends AbstractHealthIndicator {
  
  private DaraService daraService;
  
  /**
   * The health check is external and needs a dara rest resource for checking health. 
   * 
   * @param daraService The rest resource for calling the rest api of dara.
   */
  public DaraHealthIndicator(DaraService daraService) {
    this.daraService = daraService;
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.boot.actuate.health
   * .AbstractHealthIndicator#doHealthCheck(org.springframework.boot.actuate.health.Health.Builder)
   */
  @Override
  protected void doHealthCheck(Builder builder) throws Exception {
    
    //check dara health
    if (this.daraService.isDaraHealth()) {
      builder.up();
      builder.withDetail("location", this.daraService.getApiEndpoint());
    } else {
      builder.down();
    }
  }

}

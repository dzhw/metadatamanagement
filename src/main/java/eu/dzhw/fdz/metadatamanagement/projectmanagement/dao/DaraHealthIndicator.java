package eu.dzhw.fdz.metadatamanagement.projectmanagement.dao;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health.Builder;

/**
 * Health Indicator for Dara.
 * 
 * @author Daniel Katzberg
 *
 */
public class DaraHealthIndicator extends AbstractHealthIndicator {

  /*
   * (non-Javadoc)
   * @see org.springframework.boot.actuate.health
   * .AbstractHealthIndicator#doHealthCheck(org.springframework.boot.actuate.health.Health.Builder)
   */
  @Override
  protected void doHealthCheck(Builder builder) throws Exception {
    //TODO test implementation without functionality
    builder.up();
  }

}

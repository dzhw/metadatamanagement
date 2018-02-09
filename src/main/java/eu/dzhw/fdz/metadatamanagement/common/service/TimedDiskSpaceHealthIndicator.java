package eu.dzhw.fdz.metadatamanagement.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.DiskSpaceHealthIndicator;
import org.springframework.boot.actuate.health.DiskSpaceHealthIndicatorProperties;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import com.codahale.metrics.annotation.Timed;

/**
 * {@link HealthIndicator} for the Disk Space measuring the response times.
 *  
 * @author René Reitmann
 */
@Component("diskSpaceHealthIndicator")
public class TimedDiskSpaceHealthIndicator extends DiskSpaceHealthIndicator {

  @Autowired
  public TimedDiskSpaceHealthIndicator(DiskSpaceHealthIndicatorProperties properties) {
    super(properties);
  }

  @Override
  @Timed
  protected void doHealthCheck(Health.Builder builder) throws Exception {
    super.doHealthCheck(builder);
  }
}

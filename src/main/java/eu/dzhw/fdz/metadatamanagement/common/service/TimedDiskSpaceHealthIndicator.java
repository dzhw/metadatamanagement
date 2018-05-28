package eu.dzhw.fdz.metadatamanagement.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.DiskSpaceHealthIndicator;
import org.springframework.boot.actuate.health.DiskSpaceHealthIndicatorProperties;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import com.codahale.metrics.annotation.Timed;

import lombok.extern.slf4j.Slf4j;

/**
 * {@link HealthIndicator} for the Disk Space measuring the response times.
 *  
 * @author René Reitmann
 */
@Component("diskSpaceHealthIndicator")
@Slf4j
public class TimedDiskSpaceHealthIndicator extends DiskSpaceHealthIndicator {

  @Autowired
  public TimedDiskSpaceHealthIndicator(DiskSpaceHealthIndicatorProperties properties) {
    super(properties);
  }

  @Override
  @Timed
  protected void doHealthCheck(Health.Builder builder) throws Exception {
    try {
      super.doHealthCheck(builder);      
    } catch (Exception e) {
      log.error("Disk Space health check failed:", e);
      builder.down(e);
    }
  }
}

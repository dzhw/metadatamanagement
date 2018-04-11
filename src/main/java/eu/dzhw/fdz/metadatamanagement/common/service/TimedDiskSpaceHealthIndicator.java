package eu.dzhw.fdz.metadatamanagement.common.service;

import java.io.File;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.system.DiskSpaceHealthIndicator;
import org.springframework.stereotype.Component;

import com.codahale.metrics.annotation.Timed;

/**
 * {@link HealthIndicator} for the Disk Space measuring the response times.
 *  
 * @author René Reitmann
 */
@Component("diskSpaceHealthIndicator")
public class TimedDiskSpaceHealthIndicator extends DiskSpaceHealthIndicator {

  public TimedDiskSpaceHealthIndicator(File path, long threshold) {
    super(path, threshold);
  }

  @Override
  @Timed
  protected void doHealthCheck(Health.Builder builder) throws Exception {
    super.doHealthCheck(builder);
  }
}

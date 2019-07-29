package eu.dzhw.fdz.metadatamanagement.common.service;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.mongo.MongoHealthIndicator;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;

/**
 * {@link HealthIndicator} for MongoDB measuring the response times.
 *  
 * @author René Reitmann
 */
@Component("mongoHealthIndicator")
@Slf4j
public class TimedMongoHealthIndicator extends MongoHealthIndicator {
  public TimedMongoHealthIndicator(MongoTemplate mongoTemplate) {
    super(mongoTemplate);
  }
  
  @Override
  @Timed("mongo_health_check")
  protected void doHealthCheck(Health.Builder builder) throws Exception {
    try {
      super.doHealthCheck(builder);      
    } catch (Exception e) {
      log.error("Mongo health check failed:", e);
      builder.down(e);
    }
  }
}

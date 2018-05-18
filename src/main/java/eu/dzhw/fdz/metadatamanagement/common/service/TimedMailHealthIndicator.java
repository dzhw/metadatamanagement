package eu.dzhw.fdz.metadatamanagement.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.MailHealthIndicator;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import com.codahale.metrics.annotation.Timed;

import lombok.extern.slf4j.Slf4j;

/**
 * {@link HealthIndicator} for the E-Mail Service measuring the response times.
 *  
 * @author René Reitmann
 */
@Component("mailHealthIndicator")
@Slf4j
public class TimedMailHealthIndicator extends MailHealthIndicator {

  @Autowired
  public TimedMailHealthIndicator(JavaMailSenderImpl mailSender) {
    super(mailSender);
  }
  
  @Timed
  protected void doHealthCheck(Builder builder) throws Exception {
    try {
      super.doHealthCheck(builder);      
    } catch (Exception e) {
      log.error("Mail health check failed:", e);
      builder.down(e);
    }
  }
}

package eu.dzhw.fdz.metadatamanagement.common.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.core.env.Environment;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import eu.dzhw.fdz.metadatamanagement.common.config.Constants;

/**
 * Ping the message broker (rabbitMq when running in the cloud) for the health check.
 * 
 * @author René Reitmann
 */
@Component
public class MessageBrokerHealthIndicator extends AbstractHealthIndicator {

  private final SimpMessagingTemplate messagingTemplate;
  
  private final Environment env;

  @Autowired
  public MessageBrokerHealthIndicator(SimpMessagingTemplate messagingTemplate, Environment env) {
    this.messagingTemplate = messagingTemplate;
    this.env = env;
  }

  @Override
  protected void doHealthCheck(Builder builder) throws Exception {
    if (env.acceptsProfiles(Constants.SPRING_PROFILE_LOCAL)) {
      builder.withDetail("Message Broker", "Simple (local)");
    } else {
      builder.withDetail("Message Broker", "CloudAMQP (RabbitMQ)");
    }
    messagingTemplate.convertAndSend("/topic", "Ping");
    builder.up();      
  }
}

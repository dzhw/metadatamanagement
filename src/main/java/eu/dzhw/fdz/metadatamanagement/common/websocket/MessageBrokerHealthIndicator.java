package eu.dzhw.fdz.metadatamanagement.common.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.core.env.Environment;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import eu.dzhw.fdz.metadatamanagement.common.config.Constants;
import eu.dzhw.fdz.metadatamanagement.common.websocket.repository.ActiveWebSocketSessionRepository;

/**
 * Ping the message broker (rabbitMq when running in the cloud) for the health check.
 * 
 * @author René Reitmann
 */
@Component
public class MessageBrokerHealthIndicator extends AbstractHealthIndicator {

  private final SimpMessagingTemplate messagingTemplate;
  
  private final Environment env;
  
  private final ActiveWebSocketSessionRepository activeWebSocketSessionRepository;
  
  /**
   * Create the health indicator.
   * 
   * @param messagingTemplate the messaging template for pinging the broker.
   * @param env the environment for getting the active profiles
   * @param activeWebSocketSessionRepository the repository for counting the active sessions
   */
  @Autowired
  public MessageBrokerHealthIndicator(SimpMessagingTemplate messagingTemplate, Environment env,
      ActiveWebSocketSessionRepository activeWebSocketSessionRepository) {
    this.messagingTemplate = messagingTemplate;
    this.env = env;
    this.activeWebSocketSessionRepository = activeWebSocketSessionRepository;
  }

  @Override
  protected void doHealthCheck(Builder builder) throws Exception {
    if (env.acceptsProfiles(Constants.SPRING_PROFILE_LOCAL)) {
      builder.withDetail("Message Broker", "Simple (local)");
    } else {
      builder.withDetail("Message Broker", "CloudAMQP (RabbitMQ)");
    }
    builder.withDetail("Active Websocket Sessions", activeWebSocketSessionRepository.count());
    messagingTemplate.convertAndSend("/topic", "Ping");
    builder.up();      
  }
}

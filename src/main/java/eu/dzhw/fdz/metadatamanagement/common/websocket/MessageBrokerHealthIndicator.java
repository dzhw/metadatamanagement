package eu.dzhw.fdz.metadatamanagement.common.websocket;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import eu.dzhw.fdz.metadatamanagement.common.config.Constants;
import eu.dzhw.fdz.metadatamanagement.common.websocket.repository.ActiveWebSocketSessionRepository;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Ping the message broker (rabbitMq when running in the cloud) for the health check.
 * 
 * @author René Reitmann
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class MessageBrokerHealthIndicator extends AbstractHealthIndicator {

  private final SimpMessagingTemplate messagingTemplate;

  private final Environment env;

  private final ActiveWebSocketSessionRepository activeWebSocketSessionRepository;

  @Override
  @Timed("message_broker_health_check")
  protected void doHealthCheck(Builder builder) throws Exception {
    try {
      if (env.acceptsProfiles(Profiles.of(Constants.SPRING_PROFILE_LOCAL))) {
        builder.withDetail("Message Broker", "Simple (local)");
      } else {
        builder.withDetail("Message Broker", "CloudAMQP (RabbitMQ)");
      }
      builder.withDetail("Active Websocket Sessions", activeWebSocketSessionRepository.count());
      messagingTemplate.convertAndSend("/topic", "Ping");
      builder.up();
    } catch (Exception e) {
      log.error("Message Broker health check failed:", e);
      builder.down(e);
    }
  }
}

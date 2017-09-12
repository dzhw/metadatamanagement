package eu.dzhw.fdz.metadatamanagement.common.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import eu.dzhw.fdz.metadatamanagement.common.config.WebSocketConfig;

/**
 * Listener logging new and closed web socket connections.
 *  
 * @author René Reitmann
 */
@Component
public class WebSocketConnectionListener {

  private final Logger log = LoggerFactory.getLogger(WebSocketConnectionListener.class);
  
  /**
   * React on new web socket connections. 
   * @param event The application event.
   */
  @EventListener
  public void onSessionConnectEvent(SessionConnectEvent event) {
    StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
    log.debug("New websocket connection {}",
        sha.getSessionAttributes().get(WebSocketConfig.IP_ADDRESS));
  }

  /**
   * React on closed web socket connections. 
   * @param event The application event.
   */
  @EventListener
  public void onSessionDisconnectEvent(SessionDisconnectEvent event) {
    StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
    log.debug("Closed websocket connection {}", 
        sha.getSessionAttributes().get(WebSocketConfig.IP_ADDRESS));
  }

}

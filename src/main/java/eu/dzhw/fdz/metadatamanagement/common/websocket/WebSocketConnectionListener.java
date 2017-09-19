package eu.dzhw.fdz.metadatamanagement.common.websocket;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import eu.dzhw.fdz.metadatamanagement.common.config.WebSocketConfig;
import eu.dzhw.fdz.metadatamanagement.common.websocket.domain.ActiveWebsocketSession;
import eu.dzhw.fdz.metadatamanagement.common.websocket.repository.ActiveWebSocketSessionRepository;

/**
 * Listener logging new and closed web socket connections.
 *  
 * @author René Reitmann
 */
@Component
public class WebSocketConnectionListener {
  
  private static final String BROWSER = "browser";
  private static final String BROWSER_MAJOR_VERSION = "browser-major-version";
  private static final String CLIENT_OS = "client-os";
  private static final String CLIENT_OS_VERSION = "client-os-version";
  
  @Autowired
  private ActiveWebSocketSessionRepository activeWebSocketSessionRepository;

  private final Logger log = LoggerFactory.getLogger(WebSocketConnectionListener.class);
  
  /**
   * React on new web socket connections. 
   * @param event The application event.
   */
  @EventListener
  @Async
  public void onSessionConnectEvent(SessionConnectEvent event) {
    StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
    ActiveWebsocketSession session = new ActiveWebsocketSession(
        sha.getSessionId(), 
        sha.getSessionAttributes().get(WebSocketConfig.IP_ADDRESS).toString(),
        sha.getFirstNativeHeader(BROWSER),
        sha.getFirstNativeHeader(BROWSER_MAJOR_VERSION),
        sha.getFirstNativeHeader(CLIENT_OS),
        sha.getFirstNativeHeader(CLIENT_OS_VERSION),
        sha.getAcceptVersion(),
        LocalDateTime.now());
    activeWebSocketSessionRepository.save(session);
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
    activeWebSocketSessionRepository.delete(sha.getSessionId());
    log.debug("Closed websocket connection {}", 
        sha.getSessionAttributes().get(WebSocketConfig.IP_ADDRESS));
  }

}

package eu.dzhw.fdz.metadatamanagement.common.websocket;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import eu.dzhw.fdz.metadatamanagement.common.config.WebSocketConfig;
import eu.dzhw.fdz.metadatamanagement.common.websocket.domain.ActiveWebsocketSession;
import eu.dzhw.fdz.metadatamanagement.common.websocket.repository.ActiveWebSocketSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Listener logging new and closed web socket connections.
 *  
 * @author René Reitmann
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketConnectionListener {
  
  private static final String BROWSER = "browser";
  private static final String BROWSER_MAJOR_VERSION = "browser-major-version";
  private static final String CLIENT_OS = "client-os";
  private static final String CLIENT_OS_VERSION = "client-os-version";
  
  private final ActiveWebSocketSessionRepository activeWebSocketSessionRepository;
  
  /**
   * React on new web socket connections. 
   * @param event The application event.
   */
  @EventListener
  @Async
  public void onSessionConnectEvent(SessionConnectEvent event) {
    StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
    Map<String, Object> sessionAttributes = sha.getSessionAttributes();
    String ipAddress = null;
    if (sessionAttributes != null) {
      ipAddress = sessionAttributes.get(WebSocketConfig.IP_ADDRESS).toString();
    }
    ActiveWebsocketSession session = new ActiveWebsocketSession(
        sha.getSessionId(), 
        sha.getFirstNativeHeader(BROWSER),
        sha.getFirstNativeHeader(BROWSER_MAJOR_VERSION),
        sha.getFirstNativeHeader(CLIENT_OS),
        sha.getFirstNativeHeader(CLIENT_OS_VERSION),
        sha.getAcceptVersion(),
        LocalDateTime.now());
    activeWebSocketSessionRepository.save(session);
    log.debug("New websocket connection {}", ipAddress);
  }

  /**
   * React on closed web socket connections. 
   * @param event The application event.
   */
  @EventListener
  public void onSessionDisconnectEvent(SessionDisconnectEvent event) {
    StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
    String sessionId = sha.getSessionId();
    Map<String, Object> sessionAttributes = sha.getSessionAttributes();
    if (sessionId != null) {
      activeWebSocketSessionRepository.deleteById(sessionId);
      if (sessionAttributes != null) {        
        log.debug("Closed websocket connection {}", 
            sessionAttributes.get(WebSocketConfig.IP_ADDRESS));      
      }
    }
  }

}

package eu.dzhw.fdz.metadatamanagement.usermanagement.websocket;

import java.util.Collection;

import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Controller;

import eu.dzhw.fdz.metadatamanagement.usermanagement.service.MongoDbOAuth2AuthorizationService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.websocket.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller for sending messages via websockets to users.
 * 
 * @author René Reitmann
 */
@Controller
@Slf4j
@RequiredArgsConstructor
public class UserMessagesController {
  
  private final MongoDbOAuth2AuthorizationService authService;

  /**
   * Send the given message to all users after checking the authorization of the user.
   * @param message The message to be sent.
   * @param accessToken The oauth2 accessToken of the user.
   * @return the message to the topic
   * @throws Exception Thrown if not authorized for instance.
   */
  @MessageMapping("/user-messages")
  @SendTo("/topic/user-messages")
  public MessageDto sendMessageToAllUsers(MessageDto message, 
      @Header("access_token") String accessToken) throws Exception {

    OAuth2Authorization authorization =
        authService.findByToken(accessToken, OAuth2TokenType.ACCESS_TOKEN);

    if (authorization != null
        && authorization.getAttributes().containsKey("authorities")
        && authorization.getPrincipalName() != null) {

      var authorities = (Collection<?>) authorization.getAttributes().get("authorities");
      if (authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
        message.setSender(authorization.getPrincipalName());
        log.debug("Sending message from {} to all users", message.getSender());
        return message;
      }
    }

    log.error("Unauthorized message from {} with content: {}", 
        message.getSender(), message.getText());
    throw new SessionAuthenticationException("No valid access token found!");
  }
}

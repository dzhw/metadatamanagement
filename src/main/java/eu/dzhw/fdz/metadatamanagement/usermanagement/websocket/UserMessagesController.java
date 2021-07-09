package eu.dzhw.fdz.metadatamanagement.usermanagement.websocket;

import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Controller;

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
    // TODO check if sending user is ROLE_ADMIN
    String oauth2accessToken = "";
    if (oauth2accessToken != null) {
      String authentication = "";
      if (authentication != null && authentication.contains("ROLE_ADMIN")) {
        message.setSender(authentication);
        log.debug("Sending message from {} to all users", message.getSender()); 
        return message;        
      }
    }
    log.error("Unauthorized message from {} with content: {}", 
        message.getSender(), message.getText());
    throw new SessionAuthenticationException("No valid access token found!");
  }
}

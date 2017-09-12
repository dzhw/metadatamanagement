package eu.dzhw.fdz.metadatamanagement.usermanagement.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Controller;

import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.Message;

/**
 * Controller for sending messages via websockets to users.
 * 
 * @author René Reitmann
 */
@Controller
public class UserMessagesController {

  private final Logger log = LoggerFactory.getLogger(UserMessagesController.class);
  
  @Autowired
  private TokenStore tokenStore;

  /**
   * Send the given message to all users after checking the authorization of the user.
   * @param message The message to be sent.
   * @param accessToken The oauth2 accessToken of the user.
   * @return the message to the topic
   * @throws Exception Thrown if not authorized for instance.
   */
  @MessageMapping("/user-message")
  @SendTo("/topic/user-messages")
  public Message sendMessageToAllUsers(Message message, 
      @Header("access_token") String accessToken) throws Exception {
    OAuth2AccessToken oauth2accessToken = tokenStore.readAccessToken(accessToken);
    if (oauth2accessToken != null) {
      OAuth2Authentication authentication = tokenStore.readAuthentication(oauth2accessToken);
      if (authentication != null && authentication.getAuthorities().contains(
          new SimpleGrantedAuthority("ROLE_ADMIN"))) {
        message.setSender(authentication.getUserAuthentication().getName());
        log.debug("Sending message from {} to all users", message.getSender()); 
        return message;        
      }
    }
    throw new SessionAuthenticationException("No valid access token found!");
  }
}

package eu.dzhw.fdz.metadatamanagement.usermanagement.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
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

  @MessageMapping("/user-message")
  @SendTo("/topic/user-messages")
  public Message sendMessageToAllUsers(Message message) throws Exception {
    log.debug("Got websocket message from {}", message.getSender());
    return message;
  }
}

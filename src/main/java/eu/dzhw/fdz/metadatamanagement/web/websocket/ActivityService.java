package eu.dzhw.fdz.metadatamanagement.web.websocket;

import static eu.dzhw.fdz.metadatamanagement.config.WebsocketConfiguration.IP_ADDRESS;

import java.security.Principal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import eu.dzhw.fdz.metadatamanagement.security.SecurityUtils;
import eu.dzhw.fdz.metadatamanagement.web.websocket.dto.ActivityDto;

/**
 * A Controller for modification of activities.
 */
@Controller
public class ActivityService implements ApplicationListener<SessionDisconnectEvent> {

  private static final Logger log = LoggerFactory.getLogger(ActivityService.class);

  private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  @Inject
  SimpMessageSendingOperations messagingTemplate;

  /**
   * This method modify a given activity dto.
   * @param activityDto A bean for the activity representation.
   * @param stompHeaderAccessor A stomp header accessor for decoding messages from stomp frame to a
   *        stomp frame.
   * @param principal for representing any entity (security)
   * @return a modified activity dto
   */
  @SubscribeMapping("/topic/activity")
  @SendTo("/topic/tracker")
  public ActivityDto sendActivity(@Payload ActivityDto activityDto,
      StompHeaderAccessor stompHeaderAccessor, Principal principal) {
    activityDto.setUserLogin(SecurityUtils.getCurrentUserLogin());
    activityDto.setUserLogin(principal.getName());
    activityDto.setSessionId(stompHeaderAccessor.getSessionId());
    activityDto.setIpAddress(stompHeaderAccessor.getSessionAttributes().get(IP_ADDRESS).toString());
    Instant instant = Instant.ofEpochMilli(Calendar.getInstance().getTimeInMillis());
    activityDto.setTime(
        dateTimeFormatter.format(ZonedDateTime.ofInstant(instant, ZoneOffset.systemDefault())));
    log.debug("Sending user tracking data {}", activityDto);
    return activityDto;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.
   * ApplicationEvent)
   */
  @Override
  public void onApplicationEvent(SessionDisconnectEvent event) {
    ActivityDto activityDto = new ActivityDto();
    activityDto.setSessionId(event.getSessionId());
    activityDto.setPage("logout");
    messagingTemplate.convertAndSend("/topic/tracker", activityDto);
  }
}

/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.web.rest.websocket;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import eu.dzhw.fdz.metadatamanagement.BasicTest;
import eu.dzhw.fdz.metadatamanagement.config.WebsocketConfiguration;
import eu.dzhw.fdz.metadatamanagement.web.websocket.ActivityService;
import eu.dzhw.fdz.metadatamanagement.web.websocket.dto.ActivityDTO;

/**
 * @author Daniel Katzberg
 *
 */
public class ActivityServiceTest extends BasicTest {

  @Inject
  private ActivityService activityService;

  @Test
  public void testsendActivity() {
    // Arrange
    StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.createForHeartbeat();
    Map<String, Object> attributes = new HashMap<>();
    attributes.put(WebsocketConfiguration.IP_ADDRESS, "localhost");
    stompHeaderAccessor.setSessionAttributes(attributes);
    
    // Act
    ActivityDTO activityDTO = this.activityService.sendActivity(new ActivityDTO(), stompHeaderAccessor,
        Mockito.mock(Principal.class));
    
    //Assert 
    assertThat(activityDTO, not(nullValue()));
    assertThat(activityDTO.getIpAddress(), is("localhost"));
    assertThat(activityDTO.hashCode(), not(0));
  }

  @Test
  public void onApplicationContext() {
    // Arrange
    SessionDisconnectEvent sessionDisconnectEvent = Mockito.mock(SessionDisconnectEvent.class);

    // Act
    this.activityService.onApplicationEvent(sessionDisconnectEvent);

    // Assert
    assertThat(this.activityService, not(nullValue()));
  }

}

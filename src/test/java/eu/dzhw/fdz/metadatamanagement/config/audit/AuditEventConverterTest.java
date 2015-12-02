/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.config.audit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import eu.dzhw.fdz.metadatamanagement.domain.PersistentAuditEvent;

/**
 * @author Daniel Katzberg
 *
 */
public class AuditEventConverterTest {

  @Test
  public void testConvertToAuditEventWithNull() {
    // Arrange
    AuditEventConverter auditEventConverter = new AuditEventConverter();
    Iterable<PersistentAuditEvent> persistentAuditEvents = null;

    // Act
    List<AuditEvent> list = auditEventConverter.convertToAuditEvent(persistentAuditEvents);

    // Assert
    assertThat(list, not(nullValue()));
    assertThat(list.isEmpty(), is(true));
  }

  @Test
  public void testConvertDataToObjects() {
    // Arrange
    Map<String, String> data = new HashMap<>();
    data.put("OneKey", "OneValue");
    AuditEventConverter auditEventConverter = new AuditEventConverter();

    // Act
    Map<String, Object> map = auditEventConverter.convertDataToObjects(data);

    // Assert
    assertThat(map, not(nullValue()));
    assertThat(map.size(), is(1));
  }

  @Test
  public void testConvertDataToStrings() {
    // Arrange
    Map<String, Object> data = new HashMap<>();
    WebAuthenticationDetails authenticationDetails = Mockito.mock(WebAuthenticationDetails.class);
    when(authenticationDetails.getRemoteAddress()).thenReturn("localhost");
    when(authenticationDetails.getSessionId()).thenReturn("SessionId123");
    data.put("FirstKey", authenticationDetails);
    data.put("SecondKey", "secondValue");
    data.put("ThirdKey", null);
    AuditEventConverter auditEventConverter = new AuditEventConverter();

    // Act
    Map<String, String> map = auditEventConverter.convertDataToStrings(data);

    // Assert
    assertThat(map.get("remoteAddress"), is("localhost"));
    assertThat(map.get("sessionId"), is("SessionId123"));
    assertThat(map.get("SecondKey"), is("secondValue"));
    assertThat(map.get("ThirdKey"), is("null"));
  }

}

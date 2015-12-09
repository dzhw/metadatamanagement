/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.AuditEventRepository;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;

/**
 * @author Daniel Katzberg
 *
 */
public class CustomAuditEventRepositoryTest extends AbstractTest {

  @Inject
  private AuditEventRepository auditEventRepository;

  @Before
  public void before() {
    AuditEvent auditEvent = new AuditEvent("principal", "type", new HashMap<>());
    this.auditEventRepository.add(auditEvent);
  }

  @Test
  public void testFind() {
    // Arrange, see Before Method

    // Act
    List<AuditEvent> eventsByPrincipal = this.auditEventRepository.find("principal", null);
    List<AuditEvent> eventsByAll = this.auditEventRepository.find(null, null);
    LocalDateTime dateTimeToConvert = LocalDateTime.now()
      .minusDays(1);
    Instant instant = dateTimeToConvert.atZone(ZoneId.systemDefault())
      .toInstant();
    Date date = Date.from(instant);
    List<AuditEvent> events = this.auditEventRepository.find("principal", date);

    // Assert
    assertThat(eventsByPrincipal.size(), is(1));
    assertThat(eventsByPrincipal.get(0)
      .getPrincipal(), is("principal"));
    assertThat(eventsByAll.size(), is(1));
    assertThat(eventsByAll.get(0)
      .getPrincipal(), is("principal"));
    assertThat(events.size(), is(1));
    assertThat(events.get(0)
      .getPrincipal(), is("principal"));
  }



}

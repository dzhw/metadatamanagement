/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * No need for Application Conetext. No integration test.
 * 
 * @author Daniel Katzberg
 *
 */
public class PersistentAuditEventTest {

  @Test
  public void testSetId() {
    // Arrange
    PersistentAuditEvent auditEvent = new PersistentAuditEvent();

    // Act
    auditEvent.setId("Id");

    // Assert
    assertThat(auditEvent.getId(), is("Id"));
  }

}

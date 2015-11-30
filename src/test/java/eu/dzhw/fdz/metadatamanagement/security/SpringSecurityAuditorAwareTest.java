/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.security;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * No Integration Test. No need for application Context.
 * 
 * @author Daniel Katzberg
 *
 */
public class SpringSecurityAuditorAwareTest {

  @Test
  public void testGetCurrentAuditor() {
    // Arrange

    // Act
    SpringSecurityAuditorAware aware = new SpringSecurityAuditorAware();
    String auditor = aware.getCurrentAuditor();

    // Assert
    assertThat(auditor, is("anonymous"));
  }

}

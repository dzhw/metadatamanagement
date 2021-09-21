/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.usermanagement.security;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import eu.dzhw.fdz.metadatamanagement.common.config.Constants;

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
    String auditor = aware.getCurrentAuditor().get();

    // Assert
    assertThat(auditor, is(Constants.SYSTEM_ACCOUNT));
  }

}

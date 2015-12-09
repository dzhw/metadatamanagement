/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.config;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.aop.logging.LoggingAspect;

/**
 * 
 * @author Daniel Katzberg
 *
 */
public class LoggingAspectConfigurationTest extends AbstractTest {

  @Inject
  private LoggingAspectConfiguration loggingAspectConfiguration;

  @Inject
  private LoggingAspect loggingAspect;

  @PostConstruct
  public void postConstruct() {
    this.loggingAspect = new LoggingAspect();
  }

  @Test
  public void testLoggingAspect() {
    // Arrange

    // Act
    LoggingAspect loggingAspect = this.loggingAspectConfiguration.loggingAspect();

    // Assert
    assertThat(this.loggingAspectConfiguration, not(nullValue()));
    assertThat(loggingAspect, not(nullValue()));
    assertThat(this.loggingAspect, not(nullValue()));
  }
}

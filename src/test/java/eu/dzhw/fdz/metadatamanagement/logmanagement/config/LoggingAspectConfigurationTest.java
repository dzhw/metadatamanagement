/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.logmanagement.config;

import javax.inject.Inject;

import org.junit.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.logmanagement.config.LoggingAspectConfiguration;

/**
 * 
 * @author Daniel Katzberg
 *
 */
public class LoggingAspectConfigurationTest extends AbstractTest {

  @Inject
  private LoggingAspectConfiguration loggingAspectConfiguration;

  //Expected because of the wrong profile. Development is needed for the bean.
  @Test(expected=NoSuchBeanDefinitionException.class)
  public void testLoggingAspect() {
    // Arrange

    // Act
    this.loggingAspectConfiguration.loggingAspect();

    // Assert
  }
}

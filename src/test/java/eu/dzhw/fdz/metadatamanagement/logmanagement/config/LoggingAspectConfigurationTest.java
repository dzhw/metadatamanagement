/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.logmanagement.config;

import org.junit.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;

/**
 * 
 * @author Daniel Katzberg
 *
 */
public class LoggingAspectConfigurationTest extends AbstractTest {

  @Autowired
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

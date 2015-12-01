/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.config;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import eu.dzhw.fdz.metadatamanagement.aop.logging.LoggingAspect;

/**
 * 
 * @author Daniel Katzberg
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { LoggingAspectConfiguration.class, LoggingAspect.class }, loader = AnnotationConfigContextLoader.class)
@ActiveProfiles(Constants.SPRING_PROFILE_DEVELOPMENT)
public class LoggingAspectConfigurationTest {

	@Inject
	private LoggingAspectConfiguration loggingAspectConfiguration;
	
	@Inject
	private LoggingAspect loggingAspect;

	@Test
	public void testLoggingAspect() {
		//Arrange
		
		//Act
		LoggingAspect loggingAspect = this.loggingAspectConfiguration.loggingAspect();
		
		//Assert
		assertThat(this.loggingAspectConfiguration, not(nullValue()));
		assertThat(loggingAspect, not(nullValue()));
		assertThat(this.loggingAspect, not(nullValue()));
	}
}

package eu.dzhw.fdz.metadatamanagement;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Field;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;

import eu.dzhw.fdz.metadatamanagement.common.config.Constants;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestReflectionHelper;

/**
 * 
 */

/**
 * @author Daniel Katzberg
 *
 */
public class ApplicationTest {
  
  @Test
  public void testinitApplicationWithNoActiveProfile() throws NoSuchFieldException,
      SecurityException, IllegalArgumentException, IllegalAccessException, IOException {
    // Arrange
    Application application = new Application();
    Environment environment = Mockito.mock(Environment.class);
    String[] activeProfiles = {};
    when(environment.getActiveProfiles()).thenReturn(activeProfiles);
    Field envField = UnitTestReflectionHelper.getDeclaredFieldForTestInvocation(application.getClass(), "env");
    envField.set(application, environment);

    // Act
    application.initApplication();

    // Assert 
    assertThat(environment, not(nullValue()));
    assertThat(environment.getActiveProfiles().length, is(0));
  }
  
  @Test
  public void testinitApplicationWithDevAndProdProfile() throws NoSuchFieldException,
      SecurityException, IllegalArgumentException, IllegalAccessException, IOException {
    // Arrange
    Application application = new Application();
    Environment environment = Mockito.mock(Environment.class);
    String[] activeProfiles = {Constants.SPRING_PROFILE_DEVELOPMENT, Constants.SPRING_PROFILE_PRODUCTION};
    when(environment.getActiveProfiles()).thenReturn(activeProfiles);
    Field envField = UnitTestReflectionHelper.getDeclaredFieldForTestInvocation(application.getClass(), "env");
    envField.set(application, environment);

    // Act
    application.initApplication();

    // Assert 
    assertThat(environment, not(nullValue()));
    assertThat(environment.getActiveProfiles().length, is(2));
  }
}

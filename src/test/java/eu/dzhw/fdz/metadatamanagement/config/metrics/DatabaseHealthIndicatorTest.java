/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.config.metrics;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.actuate.health.Health;

/**
 * @author Daniel Katzberg
 *
 */
public class DatabaseHealthIndicatorTest {


  @Test
  public void testDoHealthCheck() throws Exception {
    // Arrange
    DatabaseHealthIndicator databaseHealthIndicator = new DatabaseHealthIndicator();

    // Act
    databaseHealthIndicator.doHealthCheck(Mockito.mock(Health.Builder.class));

    // Assert
    assertThat(databaseHealthIndicator, not(nullValue()));
  }

}

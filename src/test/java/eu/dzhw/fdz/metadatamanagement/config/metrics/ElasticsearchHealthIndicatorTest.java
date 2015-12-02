/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.config.metrics;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;

import javax.inject.Inject;

import org.junit.Test;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.boot.actuate.health.Status;

import eu.dzhw.fdz.metadatamanagement.BasicTest;
import io.searchbox.client.JestClient;

/**
 * @author Daniel Katzberg
 *
 */
public class ElasticsearchHealthIndicatorTest extends BasicTest {

  @Inject
  private JestClient jestClient;

  @Test
  public void testDoHealthCheck() throws Exception {
    // Arrange
    ElasticsearchHealthIndicator healthIndicator = new ElasticsearchHealthIndicator(jestClient);
    Builder builder = new Builder();
    Field field = builder.getClass().getDeclaredField("status");    
    field.setAccessible(true);
    
    
    // Act        
    healthIndicator.doHealthCheck(builder);
    Status status = (Status) field.get(builder);
    
    // Assert
    assertThat(status.getCode(), is(Status.UP.getCode()));    
    builder.down();
  }

}

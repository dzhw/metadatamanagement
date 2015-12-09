/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.config;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import javax.inject.Inject;

import org.junit.Test;
import org.springframework.cache.CacheManager;

import eu.dzhw.fdz.metadatamanagement.AbstractBasicTest;

/**
 * @author Daniel Katzberg
 *
 */
public class CacheConfigurationTest extends AbstractBasicTest {

  @Inject
  private CacheConfiguration cacheConfiguration;

  @Test
  public void testDestroy() {
    // Arrange

    // Act
    CacheManager cacheManager = this.cacheConfiguration.cacheManager();
    this.cacheConfiguration.destroy();

    // Assert
    assertThat(cacheManager, not(nullValue()));
  }

}

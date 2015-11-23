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
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 * @author Daniel Katzberg
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CacheConfiguration.class }, loader = AnnotationConfigContextLoader.class)
public class CacheConfigurationTest {

	@Inject
	private CacheConfiguration cacheConfiguration;
	
	@Test
	public void testDestroy() {
		//Arrange
				
		//Act
		CacheManager cacheManager = this.cacheConfiguration.cacheManager();
		this.cacheConfiguration.destroy();
		
		//Assert
		assertThat(cacheManager, not(nullValue()));
	}
	
}

/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.config;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;;

/**
 * @author Daniel Katzberg
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {}, loader = AnnotationConfigContextLoader.class)
public class CloudMongoDbConfigurationTest {

	@Test
	public void testMongoDbConfiguration() {
		// Arrange
		CloudMongoDbConfiguration cloudMongoDbConfiguration = new CloudMongoDbConfiguration();

		// Act
		ValidatingMongoEventListener eventListener = cloudMongoDbConfiguration.validatingMongoEventListener();
		CustomConversions customConversions = cloudMongoDbConfiguration.customConversions();
		

		// Assert
		assertThat(eventListener, not(nullValue()));
		assertThat(customConversions, not(nullValue()));
	}

}

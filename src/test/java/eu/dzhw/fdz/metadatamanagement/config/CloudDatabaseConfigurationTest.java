/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.config;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.config.java.CloudServiceConnectionFactory;
import org.springframework.cloud.config.java.ServiceConnectionFactory;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 * @author Daniel Katzberg
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {}, loader = AnnotationConfigContextLoader.class)
public class CloudDatabaseConfigurationTest {

	@Test
	public void testMongoDBFactory()
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
//		// Arrange
//		CloudDatabaseConfiguration cloudDatabaseConfiguration = new CloudDatabaseConfiguration(){
//			@Override
//			public ServiceConnectionFactory connectionFactory() {
//				return new CloudServiceConnectionFactory(Mockito.mock(Cloud.class));
//			}
//		};
//
//		// Act
//		MongoDbFactory dbFactory = cloudDatabaseConfiguration.mongoDbFactory();
//
//		// Assert
//		assertThat(cloudDatabaseConfiguration, not(nullValue()));
//		assertThat(dbFactory, is(nullValue()));
//		//Null check for the MongoDBFactory -> We are mocking the Cloud.
	}
}

/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.config;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.mongodb.DB;
import com.mongodb.Mongo;

import eu.dzhw.fdz.metadatamanagement.util.UnitTestReflectionHelper;;

/**
 * @author Daniel Katzberg
 *
 */
// TODO Added new Tests
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {}, loader = AnnotationConfigContextLoader.class)
public class CloudMongoDbConfigurationTest {

  @Test
  public void testMongoDbConfiguration() {
    // Arrange
    CloudMongoDbConfiguration cloudMongoDbConfiguration = new CloudMongoDbConfiguration();

    // Act
    ValidatingMongoEventListener eventListener =
        cloudMongoDbConfiguration.validatingMongoEventListener();
    CustomConversions customConversions = cloudMongoDbConfiguration.customConversions();

    // Assert
    assertThat(eventListener, not(nullValue()));
    assertThat(customConversions, not(nullValue()));
  }

  @Test
  public void testGetDatabaseName() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
    // Arrange
    CloudMongoDbConfiguration cloudMongoDbConfiguration = new CloudMongoDbConfiguration();
    Field mongoDbFactory = UnitTestReflectionHelper.getDeclaredFieldForTestInvocation(cloudMongoDbConfiguration.getClass(), "mongoDbFactory");
    MongoDbFactory dbFactory = Mockito.mock(MongoDbFactory.class);
    DB db = Mockito.mock(DB.class);
    when(db.getName()).thenReturn("MongoName");
    when(dbFactory.getDb()).thenReturn(db);
    mongoDbFactory.set(cloudMongoDbConfiguration, dbFactory);

    // Act
    String databaseName = cloudMongoDbConfiguration.getDatabaseName();
    
    //Assert
    assertThat(databaseName, is("MongoName"));
  }
  
  @Test
  public void testMongo() throws DataAccessException, Exception {
    // Arrange
    CloudMongoDbConfiguration cloudMongoDbConfiguration = new CloudMongoDbConfiguration();
    Field mongoDbFactory = UnitTestReflectionHelper.getDeclaredFieldForTestInvocation(cloudMongoDbConfiguration.getClass(), "mongoDbFactory");
    MongoDbFactory dbFactory = Mockito.mock(MongoDbFactory.class);
    DB db = Mockito.mock(DB.class);
    Mongo mongo = Mockito.mock(Mongo.class);
    when(db.getMongo()).thenReturn(mongo);
    when(dbFactory.getDb()).thenReturn(db);
    mongoDbFactory.set(cloudMongoDbConfiguration, dbFactory);

    // Act
    Mongo mongoReceived = cloudMongoDbConfiguration.mongo();
    
    //Assert
    assertThat(mongoReceived, is(mongo));
  }
}

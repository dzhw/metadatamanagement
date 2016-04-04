/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.common.config;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.mongodb.DB;
import com.mongodb.Mongo;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.config.CloudMongoDbConfiguration;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestReflectionHelper;;

/**
 * @author Daniel Katzberg
 *
 */
public class CloudMongoDbConfigurationTest extends AbstractTest {

  @Autowired 
  private LocalValidatorFactoryBean validator;
  
  @Test
  public void testMongoDbConfiguration() {
    // Arrange
    CloudMongoDbConfiguration cloudMongoDbConfiguration = new CloudMongoDbConfiguration();

    // Act
    ValidatingMongoEventListener eventListener =
        cloudMongoDbConfiguration.validatingMongoEventListener(validator);
    CustomConversions customConversions = cloudMongoDbConfiguration.customConversions();

    // Assert
    assertThat(eventListener, not(nullValue()));
    assertThat(customConversions, not(nullValue()));
  }

  @Test
  public void testGetDatabaseName() throws NoSuchFieldException, SecurityException,
      IllegalArgumentException, IllegalAccessException {
    // Arrange
    CloudMongoDbConfiguration cloudMongoDbConfiguration = new CloudMongoDbConfiguration();
    Field mongoDbFactory = UnitTestReflectionHelper
      .getDeclaredFieldForTestInvocation(cloudMongoDbConfiguration.getClass(), "mongoDbFactory");
    MongoDbFactory dbFactory = Mockito.mock(MongoDbFactory.class);
    DB db = Mockito.mock(DB.class);
    when(db.getName()).thenReturn("MongoName");
    when(dbFactory.getDb()).thenReturn(db);
    mongoDbFactory.set(cloudMongoDbConfiguration, dbFactory);

    // Act
    String databaseName = cloudMongoDbConfiguration.getDatabaseName();

    // Assert
    assertThat(databaseName, is("MongoName"));
  }

  @Test
  public void testMongo() throws DataAccessException, Exception {
    // Arrange
    CloudMongoDbConfiguration cloudMongoDbConfiguration = new CloudMongoDbConfiguration();
    Field mongoDbFactory = UnitTestReflectionHelper
      .getDeclaredFieldForTestInvocation(cloudMongoDbConfiguration.getClass(), "mongoDbFactory");
    MongoDbFactory dbFactory = Mockito.mock(MongoDbFactory.class);
    DB db = Mockito.mock(DB.class);
    Mongo mongo = Mockito.mock(Mongo.class);
    when(db.getMongo()).thenReturn(mongo);
    when(dbFactory.getDb()).thenReturn(db);
    mongoDbFactory.set(cloudMongoDbConfiguration, dbFactory);

    // Act
    Mongo mongoReceived = cloudMongoDbConfiguration.mongo();

    // Assert
    assertThat(mongoReceived, is(mongo));
  }
}

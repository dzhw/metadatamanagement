/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.config;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.util.ReflectionTestUtils;

import com.mongodb.DB;
import com.mongodb.Mongo;;

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
    ValidatingMongoEventListener eventListener =
        cloudMongoDbConfiguration.validatingMongoEventListener();
    CustomConversions customConversions = cloudMongoDbConfiguration.customConversions();


    // Assert
    assertThat(eventListener, not(nullValue()));
    assertThat(customConversions, not(nullValue()));
  }

  @Test
  //TODO
  public void testMongo() throws Exception {
//    
//    // Arrange
//    MongoDbFactory dbFactory = Mockito.mock(MongoDbFactory.class);
//    DB db = Mockito.mock(DB.class);
//    when(dbFactory.getDb()).thenReturn(db);
//    when(db.getName()).thenReturn("DB_Name");
//    Mongo mongo = Mockito.mock(Mongo.class);
//    when(db.getMongo()).thenReturn(mongo);
//
//    CloudMongoDbConfiguration cloudMongoDbConfiguration = new CloudMongoDbConfiguration();
//    ReflectionTestUtils.setField(cloudMongoDbConfiguration, "mongoDbFactory", dbFactory);
//
//    // Act
//    String databaseName = cloudMongoDbConfiguration.getDatabaseName();
//    Mongo mongoFromCloud = cloudMongoDbConfiguration.mongo();
//
//    // Assert
//    assertThat(databaseName, is("DB_Name"));
//    assertThat(mongoFromCloud, is(mongo));

  }

}

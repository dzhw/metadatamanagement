package eu.dzhw.fdz.metadatamanagement.searchmanagement.dao;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.dao.exception.ElasticsearchIndexCreateException;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.dao.exception.ElasticsearchIndexDeleteException;

/**
 * Test which checks index creation and mapping creation.
 * 
 * @author René Reitmann
 * @author Daniel Katzberg
 */
public class ElasticsearchDaoTest extends AbstractTest {

  public static final String TEST_INDEX = "hurz";
  private static final String NUMBER_OF_REPLICAS = "number_of_replicas";
  private static final String TEST_TYPE = "variables";

  @Autowired
  private ElasticsearchDao elasticsearchDao;

  @Autowired
  private ResourceLoader resourceLoader;

  private JsonParser jsonParser = new JsonParser();

  @Before
  public void deleteTestIndex() {
    if (elasticsearchDao.exists(TEST_INDEX)) {
      elasticsearchDao.delete(TEST_INDEX);
    }
  }

  @Test
  public void testCreateIndex() throws IOException {
    JsonObject settings = createTestIndex();

    assertThat(elasticsearchDao.getSettings(TEST_INDEX)
      .getAsJsonObject("index")
      .get(NUMBER_OF_REPLICAS),
        equalTo(settings.getAsJsonObject("index")
          .get(NUMBER_OF_REPLICAS)));
  }

  @Test(expected = ElasticsearchIndexCreateException.class)
  public void testCreateIndexWithError() {
    // Arrange

    // Act
    this.elasticsearchDao.createIndex("<WrongIndex>", null);

    // Assert
  }

  @Test
  public void testGetSettingsWithError() {
    // Arrange

    // Act
    JsonObject jsonObject = this.elasticsearchDao.getSettings("UnknownIndex");

    // Assert
    assertThat(jsonObject, is(nullValue()));
  }

  @Test
  public void testgetMappingWithError() throws IOException {

    // The error is produced by no existing index.

    // Arrange

    // Act
    JsonObject jsonObject = elasticsearchDao.getMapping(TEST_INDEX, TEST_TYPE);

    // Assert
    assertThat(jsonObject, is(nullValue()));
  }

  @Test(expected = ElasticsearchIndexDeleteException.class)
  public void testDeleteWithError() {

    // The error is produced by no existing index.

    // Arrange

    // Act
    this.elasticsearchDao.delete(TEST_INDEX);

    // Assert
  }

  private JsonObject createTestIndex() throws IOException {
    Reader reader = new InputStreamReader(
        resourceLoader.getResource("classpath:elasticsearch/testindex/settings.json")
          .getInputStream());

    JsonObject settings = jsonParser.parse(reader)
      .getAsJsonObject();
    elasticsearchDao.createIndex(TEST_INDEX, settings);

    return settings;
  }
}

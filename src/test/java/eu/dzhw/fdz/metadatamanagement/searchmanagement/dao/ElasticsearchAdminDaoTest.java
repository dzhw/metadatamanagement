package eu.dzhw.fdz.metadatamanagement.searchmanagement.dao;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ResourceLoader;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.dao.ElasticsearchAdminDao;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.dao.exception.ElasticsearchIndexCreateException;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.dao.exception.ElasticsearchIndexDeleteException;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.dao.exception.ElasticsearchPutMappingException;

/**
 * Test which checks index creation and mapping creation.
 * 
 * @author René Reitmann
 * @author Daniel Katzberg
 */
public class ElasticsearchAdminDaoTest extends AbstractTest {

  public static final String TEST_INDEX = "hurz";
  private static final String NUMBER_OF_REPLICAS = "number_of_replicas";
  private static final String TEST_TYPE = "variables";

  @Inject
  private ElasticsearchAdminDao elasticsearchAdminDao;

  @Inject
  private ResourceLoader resourceLoader;

  private JsonParser jsonParser = new JsonParser();

  @Before
  public void deleteTestIndex() {
    if (elasticsearchAdminDao.exists(TEST_INDEX)) {
      elasticsearchAdminDao.delete(TEST_INDEX);
    }
  }

  @Test
  public void testCreateIndex() throws IOException {
    JsonObject settings = createTestIndex();

    assertThat(elasticsearchAdminDao.getSettings(TEST_INDEX)
      .getAsJsonObject("index")
      .get(NUMBER_OF_REPLICAS),
        equalTo(settings.getAsJsonObject("index")
          .get(NUMBER_OF_REPLICAS)));
  }

  @Test
  public void testPutMapping() throws IOException {
    createTestIndex();

    Reader reader = new InputStreamReader(
        resourceLoader.getResource("classpath:elasticsearch/testindex/mapping.json")
          .getInputStream());

    JsonObject mapping = jsonParser.parse(reader)
      .getAsJsonObject();

    elasticsearchAdminDao.putMapping(TEST_INDEX, TEST_TYPE, mapping);

    assertThat(elasticsearchAdminDao.getMapping(TEST_INDEX, TEST_TYPE)
      .getAsJsonObject(TEST_TYPE)
      .getAsJsonObject("properties")
      .getAsJsonObject("allStringsAsNgrams"),
        equalTo(mapping.getAsJsonObject(TEST_TYPE)
          .getAsJsonObject("properties")
          .getAsJsonObject("allStringsAsNgrams")));

  }

  @Test(expected = ElasticsearchIndexCreateException.class)
  public void testCreateIndexWithError() {
    // Arrange

    // Act
    this.elasticsearchAdminDao.createIndex("<WrongIndex>", null);

    // Assert
  }

  @Test
  public void testGetSettingsWithError() {
    // Arrange

    // Act
    JsonObject jsonObject = this.elasticsearchAdminDao.getSettings("UnknownIndex");

    // Assert
    assertThat(jsonObject, is(nullValue()));
  }

  @Test(expected = ElasticsearchPutMappingException.class)
  public void testPutMappingWithError() throws IOException {

    // The error is produced by no existing index.

    // Arrange
    Reader reader = new InputStreamReader(
        resourceLoader.getResource("classpath:elasticsearch/testindex/mapping.json")
          .getInputStream());
    JsonObject mapping = jsonParser.parse(reader)
      .getAsJsonObject();

    // Act
    elasticsearchAdminDao.putMapping(TEST_INDEX, TEST_TYPE, mapping);

    // Assert
  }

  @Test
  public void testgetMappingWithError() throws IOException {

    // The error is produced by no existing index.

    // Arrange

    // Act
    JsonObject jsonObject = elasticsearchAdminDao.getMapping(TEST_INDEX, TEST_TYPE);

    // Assert
    assertThat(jsonObject, is(nullValue()));
  }

  @Test(expected = ElasticsearchIndexDeleteException.class)
  public void testDeleteWithError() {

    // The error is produced by no existing index.

    // Arrange

    // Act
    this.elasticsearchAdminDao.delete(TEST_INDEX);

    // Assert
  }

  private JsonObject createTestIndex() throws IOException {
    Reader reader = new InputStreamReader(
        resourceLoader.getResource("classpath:elasticsearch/testindex/settings.json")
          .getInputStream());

    JsonObject settings = jsonParser.parse(reader)
      .getAsJsonObject();
    elasticsearchAdminDao.createIndex(TEST_INDEX, settings);

    return settings;
  }
}

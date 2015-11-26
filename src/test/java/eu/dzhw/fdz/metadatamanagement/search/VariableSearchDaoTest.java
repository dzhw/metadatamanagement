package eu.dzhw.fdz.metadatamanagement.search;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonObject;

import eu.dzhw.fdz.metadatamanagement.BasicTest;
import eu.dzhw.fdz.metadatamanagement.search.document.VariableSearchDocument;
import eu.dzhw.fdz.metadatamanagement.search.document.builders.VariableSearchDocumentBuilder;
import io.searchbox.client.JestClient;

/**
 * Test for writing {@link VariableSearchDocument}s with the {@link JestClient}
 *
 * @author René Reitmann
 */
public class VariableSearchDaoTest extends BasicTest {
  @Inject
  private VariableSearchDao variableSearchDao;
  
  @Inject
  private ElasticsearchAdminDao elasticsearchAdminDao;

  @Before
  public void setupTestIndex() {
    if (elasticsearchAdminDao.exists(ElasticsearchAdminDaoTest.TEST_INDEX)) {
      elasticsearchAdminDao.delete(ElasticsearchAdminDaoTest.TEST_INDEX);
    }
    elasticsearchAdminDao.createIndex(ElasticsearchAdminDaoTest.TEST_INDEX, new JsonObject());
  }
  
  @Test
  public void testSaveSingleDocument() {
    VariableSearchDocument variableSearchDocument =
        new VariableSearchDocumentBuilder().withId("1234").withDataType("string").withLabel("label")
            .withName("name").withScaleLevel("nominal").build();
    variableSearchDao.save(variableSearchDocument, ElasticsearchAdminDaoTest.TEST_INDEX);
    
    elasticsearchAdminDao.refresh(ElasticsearchAdminDaoTest.TEST_INDEX);
    
    assertThat(variableSearchDao.findAll(ElasticsearchAdminDaoTest.TEST_INDEX).size(),equalTo(1));
  }
  
  
  @Test
  public void testBulkSaveDocuments() {
    VariableSearchDocument variableSearchDocument1 =
        new VariableSearchDocumentBuilder().withId("1234").withDataType("string").withLabel("label")
            .withName("name").withScaleLevel("nominal").build();
    VariableSearchDocument variableSearchDocument2 =
        new VariableSearchDocumentBuilder().withId("5678").withDataType("string").withLabel("label")
            .withName("name").withScaleLevel("nominal").build();
    
    List<VariableSearchDocument> documents = new ArrayList<>();
    documents.add(variableSearchDocument1);
    documents.add(variableSearchDocument2);
    
    variableSearchDao.save(documents, ElasticsearchAdminDaoTest.TEST_INDEX);
    
    elasticsearchAdminDao.refresh(ElasticsearchAdminDaoTest.TEST_INDEX);
    
    assertThat(variableSearchDao.findAll(ElasticsearchAdminDaoTest.TEST_INDEX).size(),equalTo(2));
  }
  
  @Test
  public void testDeleteDocument() {
    VariableSearchDocument variableSearchDocument =
        new VariableSearchDocumentBuilder().withId("1234").withDataType("string").withLabel("label")
            .withName("name").withScaleLevel("nominal").build();
    variableSearchDao.save(variableSearchDocument, ElasticsearchAdminDaoTest.TEST_INDEX);
    
    elasticsearchAdminDao.refresh(ElasticsearchAdminDaoTest.TEST_INDEX);
    
    assertThat(variableSearchDao.findAll(ElasticsearchAdminDaoTest.TEST_INDEX).size(),equalTo(1));
    
    variableSearchDao.delete(variableSearchDocument.getId(), ElasticsearchAdminDaoTest.TEST_INDEX);
    
    elasticsearchAdminDao.refresh(ElasticsearchAdminDaoTest.TEST_INDEX);
    
    assertThat(variableSearchDao.findAll(ElasticsearchAdminDaoTest.TEST_INDEX).size(),equalTo(0));
  }
}

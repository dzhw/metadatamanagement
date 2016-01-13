package eu.dzhw.fdz.metadatamanagement.search;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.search.document.VariableSearchDocument;
import eu.dzhw.fdz.metadatamanagement.search.document.builders.VariableSearchDocumentBuilder;
import eu.dzhw.fdz.metadatamanagement.search.exception.ElasticsearchDocumentDeleteException;
import eu.dzhw.fdz.metadatamanagement.search.exception.ElasticsearchDocumentSaveException;
import eu.dzhw.fdz.metadatamanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.service.enums.ElasticsearchIndices;
import io.searchbox.client.JestClient;

/**
 * Test for writing {@link VariableSearchDocument}s with the {@link JestClient}
 *
 * @author René Reitmann
 * @author Daniel Katzberg
 */
public class VariableSearchDaoTest extends AbstractTest {
  @Inject
  private VariableSearchDao variableSearchDao;
  
  @Inject
  private ElasticsearchAdminService elasticsearchAdminService;

  @Inject
  private ElasticsearchAdminDao elasticsearchAdminDao;

  @Before
  public void setupTestIndex() {
    this.elasticsearchAdminService.recreateAllIndices();
  }

  @Test
  public void testSaveSingleDocument() {
    VariableSearchDocument variableSearchDocument =
        new VariableSearchDocumentBuilder().withId("1234")
          .withDataType("string")
          .withLabel("label")
          .withName("name")
          .withScaleLevel("nominal")
          .build();
    variableSearchDao.save(variableSearchDocument, ElasticsearchIndices.METADATA_DE.getIndexName());

    elasticsearchAdminDao.refresh(ElasticsearchIndices.METADATA_DE.getIndexName());

    assertThat(variableSearchDao.findAll(ElasticsearchIndices.METADATA_DE.getIndexName())
      .size(), equalTo(1));
  }


  @Test
  public void testBulkSaveDocuments() {
    VariableSearchDocument variableSearchDocument1 =
        new VariableSearchDocumentBuilder().withId("1234")
          .withDataType("string")
          .withLabel("label")
          .withName("name")
          .withScaleLevel("nominal")
          .build();
    VariableSearchDocument variableSearchDocument2 =
        new VariableSearchDocumentBuilder().withId("5678")
          .withDataType("string")
          .withLabel("label")
          .withName("name")
          .withScaleLevel("nominal")
          .build();

    List<VariableSearchDocument> documents = new ArrayList<>();
    documents.add(variableSearchDocument1);
    documents.add(variableSearchDocument2);

    variableSearchDao.save(documents, ElasticsearchIndices.METADATA_DE.getIndexName());

    elasticsearchAdminDao.refresh(ElasticsearchIndices.METADATA_DE.getIndexName());

    assertThat(variableSearchDao.findAll(ElasticsearchIndices.METADATA_DE.getIndexName())
      .size(), equalTo(2));
  }

  @Test
  public void testDeleteDocument() {
    VariableSearchDocument variableSearchDocument =
        new VariableSearchDocumentBuilder().withId("1234")
          .withDataType("string")
          .withLabel("label")
          .withName("name")
          .withScaleLevel("nominal")
          .build();
    variableSearchDao.save(variableSearchDocument, ElasticsearchIndices.METADATA_DE.getIndexName());

    elasticsearchAdminDao.refresh(ElasticsearchIndices.METADATA_DE.getIndexName());

    assertThat(variableSearchDao.findAll(ElasticsearchIndices.METADATA_DE.getIndexName())
      .size(), equalTo(1));

    variableSearchDao.delete(variableSearchDocument.getId(), ElasticsearchIndices.METADATA_DE.getIndexName());

    elasticsearchAdminDao.refresh(ElasticsearchIndices.METADATA_DE.getIndexName());

    assertThat(variableSearchDao.findAll(ElasticsearchIndices.METADATA_DE.getIndexName())
      .size(), equalTo(0));
  }

  @Test(expected = ElasticsearchDocumentSaveException.class)
  public void testSaveWithError() {
    // Arrange
    VariableSearchDocument variableSearchDocument = null;

    // Act
    this.variableSearchDao.save(variableSearchDocument, ElasticsearchIndices.METADATA_DE.getIndexName());

    // Assert
  }

  @Test
  public void testSaveListWithNullList() {
    // Arrange
    List<VariableSearchDocument> variableSearchDocumentList = null;

    // Act
    this.variableSearchDao.save(variableSearchDocumentList, ElasticsearchIndices.METADATA_DE.getIndexName());

    // Assert
  }


  @Test(expected = ElasticsearchDocumentSaveException.class)
  public void testSaveListWithError() {
    // Arrange
    List<VariableSearchDocument> variableSearchDocumentList = new ArrayList<>();
    variableSearchDocumentList.add(null);

    // Act
    this.variableSearchDao.save(variableSearchDocumentList, ElasticsearchIndices.METADATA_DE.getIndexName());

    // Assert
  }

  @Test
  public void testFindAllWithError() {
    // Arrange

    // Act
    List<VariableSearchDocument> emptyResults = this.variableSearchDao.findAll("WrongIndex");

    // Assert
    assertThat(emptyResults, not(nullValue()));
    assertThat(emptyResults.isEmpty(), is(true));
  }

  @Test(expected = ElasticsearchDocumentDeleteException.class)
  public void testDeleteWithError() {
    // Arrange

    // Act
    this.variableSearchDao.delete("AnyWrongId", ElasticsearchIndices.METADATA_DE.getIndexName());

    // Assert
  }
  
  @Test
  public void testDeleteByFdzProjectName() {
    String fdzProjectName = "FDZ Project";
    VariableSearchDocument variableSearchDocument =
        new VariableSearchDocumentBuilder().withId("1234")
          .withDataType("string")
          .withLabel("label")
          .withName("name")
          .withScaleLevel("nominal")
          .withFdzProjectName(fdzProjectName)
          .build();
    variableSearchDao.save(variableSearchDocument, ElasticsearchIndices.METADATA_DE.getIndexName());
    elasticsearchAdminDao.refresh(ElasticsearchIndices.METADATA_DE.getIndexName());
    variableSearchDao.deleteByFdzProjectName(fdzProjectName, ElasticsearchIndices.METADATA_DE.getIndexName());
    elasticsearchAdminDao.refresh(ElasticsearchIndices.METADATA_DE.getIndexName());
    
    assertThat(variableSearchDao.findAll(ElasticsearchIndices.METADATA_DE.getIndexName()).size(),equalTo(0));
  }
  
  @Test
  public void testDeleteByFdzProjectNameWithTwoSimilarProjects() {
    
    //Arrange
    String fdzProjectName = "FDZ Project";
    String fdzProjectNameSimilar = "FDZ Projects";
    VariableSearchDocument variableSearchDocument =
        new VariableSearchDocumentBuilder().withId("1234")
          .withDataType("string")
          .withLabel("label")
          .withName("name")
          .withScaleLevel("nominal")
          .withFdzProjectName(fdzProjectName)
          .build();
    VariableSearchDocument variableSearchDocumentSimilar =
        new VariableSearchDocumentBuilder().withId("1234S")
          .withDataType("string")
          .withLabel("labelS")
          .withName("nameS")
          .withScaleLevel("nominalS")
          .withFdzProjectName(fdzProjectNameSimilar)
          .build();
    
    //Act
    //Save variables
    variableSearchDao.save(variableSearchDocument, ElasticsearchIndices.METADATA_DE.getIndexName());
    variableSearchDao.save(variableSearchDocumentSimilar, ElasticsearchIndices.METADATA_DE.getIndexName());
    elasticsearchAdminDao.refresh(ElasticsearchIndices.METADATA_DE.getIndexName());    
    List<VariableSearchDocument> documentsBeforeDeletion = variableSearchDao.findAll(ElasticsearchIndices.METADATA_DE.getIndexName());
    
    //Delete one Variable
    variableSearchDao.deleteByFdzProjectName(fdzProjectName, ElasticsearchIndices.METADATA_DE.getIndexName());
    elasticsearchAdminDao.refresh(ElasticsearchIndices.METADATA_DE.getIndexName());    
    List<VariableSearchDocument> documents = variableSearchDao.findAll(ElasticsearchIndices.METADATA_DE.getIndexName());
    
    //Assert
    assertThat(documentsBeforeDeletion.size(),equalTo(2));
    assertThat(documents.size(),equalTo(1));
    assertThat(documents.get(0).getFdzProjectName(),equalTo(fdzProjectNameSimilar));
  }
  
  @Test
  public void testDeleteBySurveyId() {
    String surveyId = "12345678";
    VariableSearchDocument variableSearchDocument =
        new VariableSearchDocumentBuilder().withId("1234")
          .withDataType("string")
          .withLabel("label")
          .withName("name")
          .withScaleLevel("nominal")
          .withSurveyId(surveyId)
          .build();
    variableSearchDao.save(variableSearchDocument, ElasticsearchIndices.METADATA_DE.getIndexName());
    elasticsearchAdminDao.refresh(ElasticsearchIndices.METADATA_DE.getIndexName());
    variableSearchDao.deleteBySurveyId(surveyId, ElasticsearchIndices.METADATA_DE.getIndexName());
    elasticsearchAdminDao.refresh(ElasticsearchIndices.METADATA_DE.getIndexName());
    
    
    assertThat(variableSearchDao.findAll(ElasticsearchIndices.METADATA_DE.getIndexName()).size(),equalTo(0));
  }
  
  
  @Test
  public void testDeleteBySurveyIdWithTwoSimilarSurveyIds() {
    //Arrange
    String surveyId = "12345678";
    String surveyIdSimilar = "123456789";
    VariableSearchDocument variableSearchDocument =
        new VariableSearchDocumentBuilder().withId("1234")
          .withDataType("string")
          .withLabel("label")
          .withName("name")
          .withScaleLevel("nominal")
          .withSurveyId(surveyId)
          .build();
    VariableSearchDocument variableSearchDocumentSimilar =
        new VariableSearchDocumentBuilder().withId("1234S")
          .withDataType("string")
          .withLabel("labelS")
          .withName("nameS")
          .withScaleLevel("nominal")
          .withSurveyId(surveyIdSimilar)
          .build();
    
    //Act
    //Save Variables
    variableSearchDao.save(variableSearchDocument, ElasticsearchIndices.METADATA_DE.getIndexName());
    variableSearchDao.save(variableSearchDocumentSimilar, ElasticsearchIndices.METADATA_DE.getIndexName());
    elasticsearchAdminDao.refresh(ElasticsearchIndices.METADATA_DE.getIndexName());
    List<VariableSearchDocument> documentsBeforeDeletion = variableSearchDao.findAll(ElasticsearchIndices.METADATA_DE.getIndexName());
    
    //Deletion only one Variable
    variableSearchDao.deleteBySurveyId(surveyId, ElasticsearchIndices.METADATA_DE.getIndexName());
    elasticsearchAdminDao.refresh(ElasticsearchIndices.METADATA_DE.getIndexName());
    List<VariableSearchDocument> documents = variableSearchDao.findAll(ElasticsearchIndices.METADATA_DE.getIndexName());
    
    //Assertion
    assertThat(documentsBeforeDeletion.size(),equalTo(2));
    assertThat(variableSearchDao.findAll(ElasticsearchIndices.METADATA_DE.getIndexName()).size(),equalTo(1));
    assertThat(documents.get(0).getSurveyId(),equalTo(surveyIdSimilar));
    
  }
}

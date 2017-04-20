package eu.dzhw.fdz.metadatamanagement.datasetmanagement.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.JsonSyntaxException;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.rest.TestUtil;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

/**
 * @author Daniel Katzberg
 *
 */
public class DataSetResourceTest extends AbstractTest {
  private static final String API_DATASETS_URI = "/api/data-sets";

  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;

  @Autowired
  private SurveyRepository surveyRepository;

  @Autowired
  private DataSetRepository dataSetRepository;
  
  @Autowired
  private ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;
  
  @Autowired
  private ElasticsearchAdminService elasticsearchAdminService;

  private MockMvc mockMvc;

  @Before
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
      .build();
  }

  @After
  public void cleanUp() {
    this.dataAcquisitionProjectRepository.deleteAll();
    this.surveyRepository.deleteAll();
    this.dataSetRepository.deleteAll();
    this.elasticsearchUpdateQueueService.clearQueue();
    this.elasticsearchAdminService.recreateAllIndices();
  }


  @Test
  public void testCreateDataSet() throws Exception {

    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    this.dataAcquisitionProjectRepository.save(project);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());

    DataSet dataSet = UnitTestCreateDomainObjectUtils.buildDataSet(project.getId(), survey.getId(), 1);

    // Act and Assert
    // create the variable with the given id
    mockMvc.perform(put(API_DATASETS_URI + "/" + dataSet.getId())
      .content(TestUtil.convertObjectToJsonBytes(dataSet)))
      .andExpect(status().isCreated());

    // check that auditing attributes have been set
    mockMvc.perform(get(API_DATASETS_URI + "/" + dataSet.getId()))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.createdDate", not(isEmptyOrNullString())))
      .andExpect(jsonPath("$.lastModifiedDate", not(isEmptyOrNullString())))      
      .andExpect(jsonPath("$.createdBy", is("system")))      
      .andExpect(jsonPath("$.lastModifiedBy", is("system")));

    // call toString for test coverage :-)
    dataSet.toString();
    
    elasticsearchUpdateQueueService.processAllQueueItems();

    // check that there is one data set documents
    elasticsearchAdminService.refreshAllIndices();
    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(1.0));
  }

  @Test
  public void testCreateDataSetWithSurveyButWithoutProject() throws Exception {
    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    this.dataAcquisitionProjectRepository.save(project);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    this.surveyRepository.save(survey);

    DataSet dataSet = UnitTestCreateDomainObjectUtils.buildDataSet(null, survey.getId(), 1);


    // Act and Assert
    // create the DataSet with a survey but without a project
    mockMvc.perform(put(API_DATASETS_URI + "/" + dataSet.getId())
      .content(TestUtil.convertObjectToJsonBytes(dataSet)))
      .andExpect(status().isBadRequest());
  }
  
  @Test
  public void testCreateDataSetWithWrongFormat() throws Exception {
    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    this.dataAcquisitionProjectRepository.save(project);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());

    DataSet dataSet = UnitTestCreateDomainObjectUtils.buildDataSet(null, survey.getId(), 1);
    dataSet.setFormat(new I18nString("wrong", "wrong"));

    // Act and Assert
    // create the DataSet with a survey but without a project
    mockMvc.perform(put(API_DATASETS_URI + "/" + dataSet.getId())
      .content(TestUtil.convertObjectToJsonBytes(dataSet)))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void testCreateDataSetWithUnknownSurvey() throws Exception {
    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    this.dataAcquisitionProjectRepository.save(project);

    DataSet dataSet = UnitTestCreateDomainObjectUtils.buildDataSet(project.getId(), "notExist", null);

    // Act and Assert
    // create the DataSet with the given id but with an unknown survey
    mockMvc.perform(put(API_DATASETS_URI + "/" + dataSet.getId())
      .content(TestUtil.convertObjectToJsonBytes(dataSet)))
      .andExpect(status().is2xxSuccessful());
  }

  @Test
  public void testCreateDataSetWithSurveyFromDifferentProject() throws Exception {

    // Arrange
    DataAcquisitionProject project1 = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    DataAcquisitionProject project2 = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    project2.setId("testProject2");
    this.dataAcquisitionProjectRepository.save(project1);
    this.dataAcquisitionProjectRepository.save(project2);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project2.getId());
    this.surveyRepository.save(survey);

    DataSet dataSet =
        UnitTestCreateDomainObjectUtils.buildDataSet(project1.getId(), survey.getId(), 1);
    // Act and Assert
    // create the DataSet with the given id but with a survey from a different project
    mockMvc.perform(put(API_DATASETS_URI + "/" + dataSet.getId())
      .content(TestUtil.convertObjectToJsonBytes(dataSet)))
      .andExpect(status().is2xxSuccessful()).andReturn();
  }

  @Test
  public void testDeleteDataSet() throws JsonSyntaxException, IOException, Exception {
    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    this.dataAcquisitionProjectRepository.save(project);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());

    DataSet dataSet = UnitTestCreateDomainObjectUtils.buildDataSet(project.getId(), survey.getId(), 1);

    // create the DataSet with the given id
    mockMvc.perform(put(API_DATASETS_URI + "/" + dataSet.getId())
      .content(TestUtil.convertObjectToJsonBytes(dataSet)))
      .andExpect(status().isCreated());

    // delete the DataSet
    mockMvc.perform(delete(API_DATASETS_URI + "/" + dataSet.getId()))
      .andExpect(status().is2xxSuccessful());

    // check that the DataSet has been deleted
    mockMvc.perform(get(API_DATASETS_URI + "/" + dataSet.getId()))
      .andExpect(status().isNotFound());
    
    elasticsearchUpdateQueueService.processAllQueueItems();

    // check that there are no more data set documents
    elasticsearchAdminService.refreshAllIndices();
    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(0.0));
  }

  @Test
  public void testUpdateDataSet() throws Exception {
    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    this.dataAcquisitionProjectRepository.save(project);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());

    DataSet dataSet = UnitTestCreateDomainObjectUtils.buildDataSet(project.getId(), survey.getId(), 1);

    // Act and Assert
    // create the DataSet with the given id
    mockMvc.perform(put(API_DATASETS_URI + "/" + dataSet.getId())
      .content(TestUtil.convertObjectToJsonBytes(dataSet)))
      .andExpect(status().isCreated());

    dataSet.getDescription()
      .setDe("Angepasst.");

    // update the DataSet with the given id
    mockMvc.perform(put(API_DATASETS_URI + "/" + dataSet.getId())
      .content(TestUtil.convertObjectToJsonBytes(dataSet)))
      .andExpect(status().is2xxSuccessful());

    // read the updated DataSet and check the version
    mockMvc.perform(get(API_DATASETS_URI + "/" + dataSet.getId() + "?projection=complete"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(dataSet.getId())))
      .andExpect(jsonPath("$.version", is(1)))
      .andExpect(jsonPath("$.description.de", is("Angepasst.")));
    
    elasticsearchUpdateQueueService.processAllQueueItems();

    // check that there is one data set documents
    elasticsearchAdminService.refreshAllIndices();
    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(1.0));
  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testDeletingProjectDeletesDataSet() throws Exception {
    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    this.dataAcquisitionProjectRepository.save(project);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    this.surveyRepository.save(survey);

    DataSet dataSet = UnitTestCreateDomainObjectUtils.buildDataSet(project.getId(), survey.getId(), 1);

    // Act and Assert
    // create the DataSet with the given id
    mockMvc.perform(put(API_DATASETS_URI + "/" + dataSet.getId())
      .content(TestUtil.convertObjectToJsonBytes(dataSet)))
      .andExpect(status().isCreated());

    mockMvc.perform(delete("/api/data-acquisition-projects/" + project.getId()))
      .andExpect(status().is2xxSuccessful());

    // check that the DataSet has been deleted
    mockMvc.perform(get(API_DATASETS_URI + "/" + dataSet.getId()))
      .andExpect(status().isNotFound());
  }


}

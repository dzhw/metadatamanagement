package eu.dzhw.fdz.metadatamanagement.datasetmanagement.rest;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.JsonSyntaxException;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.rest.TestUtil;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
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
@Disabled
@WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
public class DataSetResourceControllerTest extends AbstractTest {
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

  @Autowired
  private JaversService javersService;

  private MockMvc mockMvc;

  @BeforeEach
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
      .build();
  }

  @AfterEach
  public void cleanUp() {
    this.dataAcquisitionProjectRepository.deleteAll();
    this.surveyRepository.deleteAll();
    this.dataSetRepository.deleteAll();
    this.elasticsearchUpdateQueueService.clearQueue();
    this.elasticsearchAdminService.recreateAllIndices();
    this.javersService.deleteAll();
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
      .content(TestUtil.convertObjectToJsonBytes(dataSet)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated());

    // check that auditing attributes have been set
    mockMvc.perform(get(API_DATASETS_URI + "/" + dataSet.getId()))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.createdDate", not(isEmptyOrNullString())))
      .andExpect(jsonPath("$.lastModifiedDate", not(isEmptyOrNullString())))
      .andExpect(jsonPath("$.createdBy", is("user")))
      .andExpect(jsonPath("$.lastModifiedBy", is("user")));

    // call toString for test coverage :-)
    dataSet.toString();

    elasticsearchUpdateQueueService.processAllQueueItems();

    // check that there is one data set documents
    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(1L));
  }

  @Test
  public void testCreateDataSetWithPost() throws Exception {

    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    this.dataAcquisitionProjectRepository.save(project);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());

    DataSet dataSet =
        UnitTestCreateDomainObjectUtils.buildDataSet(project.getId(), survey.getId(), 1);

    // Act and Assert
    // create the variable with the given id
    mockMvc.perform(post(API_DATASETS_URI).content(TestUtil.convertObjectToJsonBytes(dataSet))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());

    // check that auditing attributes have been set
    mockMvc.perform(get(API_DATASETS_URI + "/" + dataSet.getId())).andExpect(status().isOk())
        .andExpect(jsonPath("$.createdDate", not(isEmptyOrNullString())))
        .andExpect(jsonPath("$.lastModifiedDate", not(isEmptyOrNullString())))
        .andExpect(jsonPath("$.createdBy", is("user")))
        .andExpect(jsonPath("$.lastModifiedBy", is("user")));

    elasticsearchUpdateQueueService.processAllQueueItems();

    // check that there is one data set documents
    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(1L));
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
      .content(TestUtil.convertObjectToJsonBytes(dataSet)).contentType(MediaType.APPLICATION_JSON))
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
      .content(TestUtil.convertObjectToJsonBytes(dataSet)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void testCreateDataSetWithEmptyCitationHint() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    this.dataAcquisitionProjectRepository.save(project);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());

    DataSet dataSet = UnitTestCreateDomainObjectUtils.buildDataSet(null, survey.getId(), 1);

    // Act and Assert
    // create the DataSet with a survey but without a project
    mockMvc.perform(put(API_DATASETS_URI + "/" + dataSet.getId())
        .content(TestUtil.convertObjectToJsonBytes(dataSet)).contentType(MediaType.APPLICATION_JSON))
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
      .content(TestUtil.convertObjectToJsonBytes(dataSet)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().is2xxSuccessful());
  }

  @Test
  public void testCreateDataSetWithSurveyFromDifferentProject() throws Exception {

    // Arrange
    DataAcquisitionProject project1 = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    DataAcquisitionProject project2 = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    project2.setId("testproject2");
    project2.setMasterId("testproject2");
    this.dataAcquisitionProjectRepository.save(project1);
    this.dataAcquisitionProjectRepository.save(project2);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project2.getId());
    this.surveyRepository.save(survey);

    DataSet dataSet =
        UnitTestCreateDomainObjectUtils.buildDataSet(project1.getId(), survey.getId(), 1);
    // Act and Assert
    // create the DataSet with the given id but with a survey from a different project
    mockMvc.perform(put(API_DATASETS_URI + "/" + dataSet.getId())
      .content(TestUtil.convertObjectToJsonBytes(dataSet)).contentType(MediaType.APPLICATION_JSON))
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
      .content(TestUtil.convertObjectToJsonBytes(dataSet)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated());

    // delete the DataSet
    mockMvc.perform(delete(API_DATASETS_URI + "/" + dataSet.getId()))
      .andExpect(status().is2xxSuccessful());

    // check that the DataSet has been deleted
    mockMvc.perform(get(API_DATASETS_URI + "/" + dataSet.getId()))
      .andExpect(status().isNotFound());

    elasticsearchUpdateQueueService.processAllQueueItems();

    // check that there are no more data set documents
    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(0L));
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
      .content(TestUtil.convertObjectToJsonBytes(dataSet)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated());

    dataSet.getDescription()
      .setDe("Angepasst.");
    dataSet.setVersion(0L);

    // update the DataSet with the given id
    mockMvc.perform(put(API_DATASETS_URI + "/" + dataSet.getId())
      .content(TestUtil.convertObjectToJsonBytes(dataSet)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().is2xxSuccessful());

    // read the updated DataSet and check the version
    mockMvc.perform(get(API_DATASETS_URI + "/" + dataSet.getId()))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(dataSet.getId())))
      .andExpect(jsonPath("$.version", is(1)))
      .andExpect(jsonPath("$.description.de", is("Angepasst.")));

    elasticsearchUpdateQueueService.processAllQueueItems();

    // check that there is one data set documents
    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(1L));
  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testCreateShadowCopyDataSet() throws Exception {
    DataSet dataSet = UnitTestCreateDomainObjectUtils.buildDataSet("issue1991", "test", 1);
    dataSet.setId(dataSet.getId() + "-1.0.0");

    mockMvc.perform(post(API_DATASETS_URI)
        .content(TestUtil.convertObjectToJsonBytes(dataSet))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].message", containsString("global.error.shadow-create-not-allowed")));
  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testUpdateShadowCopyDataSet() throws Exception {
    DataSet dataSet = UnitTestCreateDomainObjectUtils.buildDataSet("issue1991", "test", 1);
    dataSet.setId(dataSet.getId() + "-1.0.0");

    dataSet = dataSetRepository.save(dataSet);

    mockMvc.perform(put(API_DATASETS_URI + "/" + dataSet.getId())
        .content(TestUtil.convertObjectToJsonBytes(dataSet))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].message", containsString("global.error.shadow-save-not-allowed")));
  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testDeleteShadowCopyDataSet() throws Exception {
    DataSet dataSet = UnitTestCreateDomainObjectUtils.buildDataSet("issue1991", "test", 1);
    dataSet.setId(dataSet.getId() + "-1.0.0");

    dataSetRepository.save(dataSet);

    mockMvc.perform(delete(API_DATASETS_URI + "/" + dataSet.getId()))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].message", containsString("global.error.shadow-delete-not-allowed")));
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
      .content(TestUtil.convertObjectToJsonBytes(dataSet)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated());

    mockMvc.perform(delete("/api/data-acquisition-projects/" + project.getId()))
      .andExpect(status().is2xxSuccessful());

    // check that the DataSet has been deleted
    mockMvc.perform(get(API_DATASETS_URI + "/" + dataSet.getId()))
      .andExpect(status().isNotFound());
  }


}

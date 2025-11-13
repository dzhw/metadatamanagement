package eu.dzhw.fdz.metadatamanagement.datasetmanagement.rest;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateValidIds;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

@Disabled
public class DeleteAllDateSetsResourceControllerTest extends AbstractTest {

  private static final String API_DELETE_ALL_QUESTIONS_URI = "/api/data-acquisition-projects";
  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;

  private MockMvc mockMvc;

  @Autowired
  private DataSetRepository dataSetRepo;

  @Autowired
  private ElasticsearchUpdateQueueItemRepository elasticsearchUpdateQueueItemRepository;

  @Autowired
  private ElasticsearchAdminService elasticsearchAdminService;

  @Autowired
  private JaversService javersService;

  @BeforeEach
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
  }

  @AfterEach
  public void cleanUp() {
    dataAcquisitionProjectRepository.deleteAll();
    dataSetRepo.deleteAll();
    javersService.deleteAll();
    elasticsearchUpdateQueueItemRepository.deleteAll();
    elasticsearchAdminService.recreateAllIndices();
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.DATA_PROVIDER)
  public void testDeleteAllDataSetsOfProject() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);
    String projectId = project.getId();

    Integer surveyNumber0 = 1;
    String surveyId0 = UnitTestCreateValidIds.buildSurveyId(projectId, surveyNumber0);
    Integer surveyNumber1 = 2;
    String surveyId1 = UnitTestCreateValidIds.buildSurveyId(projectId, surveyNumber1);
    DataSet testDataSet0 =
        UnitTestCreateDomainObjectUtils.buildDataSet(projectId, surveyId0, surveyNumber0);
    DataSet testDataSet1 =
        UnitTestCreateDomainObjectUtils.buildDataSet(projectId, surveyId1, surveyNumber1);
    testDataSet0.setId(UnitTestCreateValidIds.buildDataSetId(projectId, 1));
    testDataSet0.setMasterId(testDataSet0.getId());
    testDataSet1.setId(UnitTestCreateValidIds.buildDataSetId(projectId, 2));
    testDataSet1.setMasterId(testDataSet1.getId());
    testDataSet0.setNumber(1);
    testDataSet1.setNumber(2);
    dataSetRepo.insert(testDataSet0);
    dataSetRepo.insert(testDataSet1);
    assertEquals(2, dataSetRepo.findByDataAcquisitionProjectId(projectId).size());
    mockMvc.perform(delete(API_DELETE_ALL_QUESTIONS_URI + "/" + projectId + "/" + "data-sets"))
        .andExpect(status().isNoContent());
    assertEquals(0, dataSetRepo.findByDataAcquisitionProjectId(projectId).size());
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.DATA_PROVIDER)
  public void testDeleteAllDataSetsOfShadowCopyProject() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    String masterProjectId = project.getId();
    String shadowProjectId = masterProjectId + "-1.0.0";
    project.setId(shadowProjectId);
    dataAcquisitionProjectRepository.save(project);

    DataSet dataSet = UnitTestCreateDomainObjectUtils.buildDataSet(masterProjectId, "survey", 1);
    dataSet.setId(dataSet.getId() + "-1.0.0");
    dataSet.setDataAcquisitionProjectId(shadowProjectId);
    dataSetRepo.save(dataSet);

    mockMvc.perform(delete(API_DELETE_ALL_QUESTIONS_URI + "/" + shadowProjectId + "/" + "data-sets"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].message", containsString("global.error.shadow-delete-not-allowed")));
  }

}

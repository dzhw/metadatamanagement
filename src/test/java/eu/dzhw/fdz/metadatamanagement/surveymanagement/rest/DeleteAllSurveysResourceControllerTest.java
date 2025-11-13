package eu.dzhw.fdz.metadatamanagement.surveymanagement.rest;

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
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

@Disabled
public class DeleteAllSurveysResourceControllerTest extends AbstractTest {

  private static final String API_DELETE_ALL_QUESTIONS_URI = "/api/data-acquisition-projects";

  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;

  private MockMvc mockMvc;

  @Autowired
  private SurveyRepository surveyRepo;

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
    surveyRepo.deleteAll();
    javersService.deleteAll();
    elasticsearchUpdateQueueItemRepository.deleteAll();
    elasticsearchAdminService.recreateAllIndices();
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.DATA_PROVIDER)
  public void testDeleteAllSurveysOfProject() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);
    String projectId = project.getId();
    Survey testSurvey0 = UnitTestCreateDomainObjectUtils.buildSurvey(projectId);
    Survey testSurvey1 = UnitTestCreateDomainObjectUtils.buildSurvey(projectId);
    testSurvey0.setId(UnitTestCreateValidIds.buildSurveyId(projectId, 1));
    testSurvey0.setMasterId(testSurvey0.getId());
    testSurvey1.setId(UnitTestCreateValidIds.buildSurveyId(projectId, 2));
    testSurvey1.setMasterId(testSurvey1.getId());
    testSurvey0.setNumber(1);
    testSurvey1.setNumber(2);
    surveyRepo.insert(testSurvey0);
    surveyRepo.insert(testSurvey1);
    assertEquals(2, surveyRepo.findByDataAcquisitionProjectId(projectId).size());
    mockMvc.perform(delete(API_DELETE_ALL_QUESTIONS_URI + "/" + projectId + "/"+"surveys")).andExpect(status().isNoContent());
    assertEquals(0, surveyRepo.findByDataAcquisitionProjectId(projectId).size());
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.DATA_PROVIDER)
  public void testDeleteAllSurveysOfShadowCopyProject() throws Exception {
    String masterProjectId = "issue1991";
    String shadowProjectId = masterProjectId + "-1.0.0";
    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(masterProjectId);
    survey.setId(survey.getId() + "-1.0.0");
    survey.setDataAcquisitionProjectId(shadowProjectId);
    surveyRepo.save(survey);

    mockMvc.perform(delete(API_DELETE_ALL_QUESTIONS_URI + "/" + shadowProjectId + "/"+"surveys"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].message", containsString("global.error.shadow-delete-not-allowed")));
  }

}

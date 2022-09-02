package eu.dzhw.fdz.metadatamanagement.questionmanagement.rest;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;

public class DeleteAllQuestionsResourceControllerTest extends AbstractTest {

  private static final String API_PROJECT_URI = "/api/data-acquisition-projects";
  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;

  private MockMvc mockMvc;

  @Autowired
  private QuestionRepository questionRepo;

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
    questionRepo.deleteAll();
    javersService.deleteAll();
    elasticsearchUpdateQueueItemRepository.deleteAll();
    elasticsearchAdminService.recreateAllIndices();
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.DATA_PROVIDER)
  public void testDeleteAllQuestionsOfProject() throws Exception {
    String projectId = "theproject";
    Question question = UnitTestCreateDomainObjectUtils.buildQuestion(projectId, 1, "instrumentid");
    questionRepo.save(question);

    assertEquals(1, questionRepo.findByDataAcquisitionProjectId(projectId).size());
    mockMvc.perform(delete(API_PROJECT_URI + "/" + projectId + "/questions")).andExpect(status().isNoContent());
    assertEquals(0, questionRepo.findByDataAcquisitionProjectId(projectId).size());
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testDeleteAllShadowCopyQuestionsOfProject() throws Exception {
    String masterProjectId = "issue1991";
    String shadowCopyProjectId = masterProjectId + "-1.0.0";
    Question question = UnitTestCreateDomainObjectUtils.buildQuestion(masterProjectId,1,"instrumentid");
    question.setId(question.getId() + "-1.0.0");
    question.setDataAcquisitionProjectId(shadowCopyProjectId);
    questionRepo.save(question);

    mockMvc.perform(delete(API_PROJECT_URI + "/" + shadowCopyProjectId + "/questions"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].message", containsString("global.error.shadow-delete-not-allowed")));
  }

}

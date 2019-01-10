package eu.dzhw.fdz.metadatamanagement.questionmanagement.rest;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

public class DeleteAllQuestionsResourceControllerTest extends AbstractTest {

  private static final String API_DELETE_ALL_QUESTIONS_URI = "/api/data-acquisition-projects";
  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;

  private MockMvc mockMvc;

  @Autowired
  QuestionRepository questionRepo;
  @Autowired
  private ElasticsearchUpdateQueueItemRepository elasticsearchUpdateQueueItemRepository;

  @Autowired
  private JaversService javersService;

  @Before
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
  }

  @After
  public void cleanUp() {
    dataAcquisitionProjectRepository.deleteAll();
    questionRepo.deleteAll();
    javersService.deleteAll();
    elasticsearchUpdateQueueItemRepository.deleteAll();
  }
  @Test
  @WithMockUser(authorities = AuthoritiesConstants.DATA_PROVIDER)
  public void testDeleteAllQuestionsOfProject() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);
    String projectId = project.getId();
    int instrumentNumber = 1;
    String instrumentId = "testInstrument";
    String surveyId = "testSurvey";
    Question testQuestion0 = UnitTestCreateDomainObjectUtils.buildQuestion(projectId, instrumentNumber, instrumentId, surveyId);
    Question testQuestion1 = UnitTestCreateDomainObjectUtils.buildQuestion(projectId, instrumentNumber, instrumentId, surveyId);
    String questionNumber0 = "123.45";
    testQuestion0.setId(UnitTestCreateValidIds.buildQuestionId(projectId, instrumentNumber, questionNumber0));
    testQuestion0.setNumber(questionNumber0);
    String questionNumber1 = "123.56";
    testQuestion1.setId(UnitTestCreateValidIds.buildQuestionId(projectId, instrumentNumber, questionNumber1));
    testQuestion1.setNumber(questionNumber1);
    questionRepo.insert(testQuestion0);
    questionRepo.insert(testQuestion1);
    assertEquals(2, questionRepo.findByDataAcquisitionProjectId(projectId).size());
    mockMvc.perform(delete(API_DELETE_ALL_QUESTIONS_URI + "/" + projectId + "/"+"questions")).andExpect(status().isNoContent());
    assertEquals(0, questionRepo.findByDataAcquisitionProjectId(projectId).size());
  }

}

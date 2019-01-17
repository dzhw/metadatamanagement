package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.rest;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

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
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository.InstrumentRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

public class DeleteAllInstrumentsResourceControllerTest extends AbstractTest {

  private static final String API_DELETE_ALL_QUESTIONS_URI = "/api/data-acquisition-projects";
  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;

  private MockMvc mockMvc;

  @Autowired
  InstrumentRepository instrumentRepo;
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
    instrumentRepo.deleteAll();
    javersService.deleteAll();
    elasticsearchUpdateQueueItemRepository.deleteAll();
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.DATA_PROVIDER)
  public void testDeleteAllQuestionsOfProject() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);
    String projectId = project.getId();
    List<Integer> surveyNumbers = new ArrayList<Integer>();
    surveyNumbers.add(1);
    Instrument testInstr0 = UnitTestCreateDomainObjectUtils.buildInstrument(projectId,
        UnitTestCreateValidIds.buildSurveyId(projectId, 1));
    Instrument testInstr1 = UnitTestCreateDomainObjectUtils.buildInstrument(projectId,
        UnitTestCreateValidIds.buildSurveyId(projectId, 2));
    testInstr0.setId(UnitTestCreateValidIds.buildInstrumentId(projectId, 1));
    testInstr1.setId(UnitTestCreateValidIds.buildInstrumentId(projectId, 2));
    testInstr0.setNumber(1);
    testInstr1.setNumber(2);
    instrumentRepo.insert(testInstr0);
    instrumentRepo.insert(testInstr1);
    assertEquals(2, instrumentRepo.findByDataAcquisitionProjectId(projectId).size());
    mockMvc.perform(delete(API_DELETE_ALL_QUESTIONS_URI + "/" + projectId + "/" + "instruments"))
        .andExpect(status().isNoContent());
    assertEquals(0, instrumentRepo.findByDataAcquisitionProjectId(projectId).size());
  }

}

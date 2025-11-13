package eu.dzhw.fdz.metadatamanagement.datasetmanagement.rest;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;

@Disabled
public class AvailableDataSetNumbersResourceControllerTest extends AbstractTest {
  @Autowired
  private WebApplicationContext wac;

  private MockMvc mockMvc;

  @Autowired
  private DataSetRepository dataSetRepo;

  @Autowired
  private SurveyRepository surveyRepo;

  @Autowired
  private DataAcquisitionProjectRepository rdcProjectRepository;

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
    dataSetRepo.deleteAll();
    surveyRepo.deleteAll();
    rdcProjectRepository.deleteAll();
    javersService.deleteAll();
    elasticsearchUpdateQueueItemRepository.deleteAll();
    elasticsearchAdminService.recreateAllIndices();
  }

  @Test
  public void testAvailableNumbersIfThereAreNoDataSets() throws Exception {
    mockMvc.perform(get("/api/data-acquisition-projects/test/available-data-set-numbers"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(1)))
        .andExpect(jsonPath("$.[0]", is(1)));
  }

  @Test
  public void testAvailableNumbersIfThereIsOneDataSet() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    rdcProjectRepository.save(project);
    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    surveyRepo.save(survey);
    DataSet dataSet = UnitTestCreateDomainObjectUtils.buildDataSet(project.getId(), survey.getId(),
        survey.getNumber(), 3);
    dataSetRepo.save(dataSet);
    mockMvc
        .perform(
            get("/api/data-acquisition-projects/" + project.getId()
                + "/available-data-set-numbers"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(3)))
        .andExpect(jsonPath("$.[0]", is(1))).andExpect(jsonPath("$.[1]", is(2)))
        .andExpect(jsonPath("$.[2]", is(4)));
  }

}

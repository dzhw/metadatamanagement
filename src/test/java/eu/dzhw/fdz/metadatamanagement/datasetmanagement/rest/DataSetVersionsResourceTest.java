package eu.dzhw.fdz.metadatamanagement.datasetmanagement.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.rest.TestUtil;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;

@WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
public class DataSetVersionsResourceTest extends AbstractTest {
  private static final String API_DATASET_URI = "/api/data-sets";

  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;

  @Autowired
  private DataSetRepository dataSetRepository;

  @Autowired
  private ElasticsearchUpdateQueueItemRepository elasticsearchUpdateQueueItemRepository;

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
    dataAcquisitionProjectRepository.deleteAll();
    dataSetRepository.deleteAll();
    elasticsearchUpdateQueueItemRepository.deleteAll();
    elasticsearchAdminService.recreateAllIndices();
    javersService.deleteAll();
  }

  @Test
  public void testCreateDataSetAndReadVersions() throws IOException, Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    DataSet dataSet = UnitTestCreateDomainObjectUtils.buildDataSet(project.getId(), survey.getId(),
        survey.getNumber());

    // create the data set with the given id
    mockMvc
        .perform(put(API_DATASET_URI + "/" + dataSet.getId())
            .content(TestUtil.convertObjectToJsonBytes(dataSet))
            .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated());

    // read the data set versions
    mockMvc.perform(get(API_DATASET_URI + "/" + dataSet.getId() + "/versions"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.length()", is(equalTo(1))))
        .andExpect(jsonPath("$[0].id", is(dataSet.getId())))
        .andExpect(jsonPath("$[0].description.de", is(dataSet.getDescription().getDe())));
  }

  @Test
  public void testEditDataSetAndReadVersions() throws IOException, Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    DataSet dataSet = UnitTestCreateDomainObjectUtils.buildDataSet(project.getId(), survey.getId(),
        survey.getNumber());
    String firstDescription = dataSet.getDescription().getDe();

    // create the data set with the given id
    mockMvc
        .perform(put(API_DATASET_URI + "/" + dataSet.getId())
            .content(TestUtil.convertObjectToJsonBytes(dataSet))
            .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated());

    dataSet.setVersion(0L);
    // update the data set with the given id
    dataSet.setDescription(new I18nString("hurzDe2", "hurzEn2"));
    mockMvc
        .perform(put(API_DATASET_URI + "/" + dataSet.getId())
            .content(TestUtil.convertObjectToJsonBytes(dataSet))
            .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isNoContent());

    dataSet.setVersion(1L);
    // update the data set again with the given id
    dataSet.setDescription(new I18nString("hurzDe3", "hurzEn3"));
    mockMvc
        .perform(put(API_DATASET_URI + "/" + dataSet.getId())
            .content(TestUtil.convertObjectToJsonBytes(dataSet))
            .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isNoContent());

    // read the data set versions
    mockMvc.perform(get(API_DATASET_URI + "/" + dataSet.getId() + "/versions"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.length()", is(equalTo(3))))
        .andExpect(jsonPath("$[0].id", is(dataSet.getId())))
        .andExpect(jsonPath("$[0].description.de", is("hurzDe3")))
      .andExpect(jsonPath("$[0].version", is(equalTo(2))))
        .andExpect(jsonPath("$[1].id", is(dataSet.getId())))
        .andExpect(jsonPath("$[1].description.de", is("hurzDe2")))
      .andExpect(jsonPath("$[1].version", is(equalTo(1))))
        .andExpect(jsonPath("$[2].id", is(dataSet.getId())))
      .andExpect(jsonPath("$[2].version", is(equalTo(0))))
        .andExpect(jsonPath("$[2].description.de", is(firstDescription)));
  }

  @Test
  public void testDataSetVersionsNotFound() throws IOException, Exception {
    mockMvc.perform(get(API_DATASET_URI + "/spaß/versions")).andExpect(status().isOk())
        .andExpect(jsonPath("$.length()", is(0)));
  }
}

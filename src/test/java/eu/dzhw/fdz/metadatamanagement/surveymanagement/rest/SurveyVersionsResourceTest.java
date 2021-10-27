package eu.dzhw.fdz.metadatamanagement.surveymanagement.rest;

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
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;

@WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
public class SurveyVersionsResourceTest extends AbstractTest {
  private static final String API_SURVEY_URI = "/api/surveys";

  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;

  @Autowired
  private SurveyRepository surveyRepository;

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
    surveyRepository.deleteAll();
    elasticsearchUpdateQueueItemRepository.deleteAll();
    elasticsearchAdminService.recreateAllIndices();
    javersService.deleteAll();
  }

  @Test
  public void testCreateSurveyAndReadVersions() throws IOException, Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());

    // create the dataPackage with the given id
    mockMvc.perform(put(API_SURVEY_URI + "/" + survey.getId())
      .content(TestUtil.convertObjectToJsonBytes(survey)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated());

    // read the dataPackage versions
    mockMvc.perform(get(API_SURVEY_URI + "/" + survey.getId() + "/versions"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.length()", is(equalTo(1))))
      .andExpect(jsonPath("$[0].id", is(survey.getId())))
      .andExpect(jsonPath("$[0].title.de", is(survey.getTitle().getDe())));
  }

  @Test
  public void testEditSurveyAndReadVersions() throws IOException, Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    String firstTitle = survey.getTitle().getDe();

    // create the survey with the given id
    mockMvc.perform(put(API_SURVEY_URI + "/" + survey.getId())
      .content(TestUtil.convertObjectToJsonBytes(survey)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated());

    survey.setVersion(0L);
    // update the dataPackage with the given id
    survey.setTitle(new I18nString("hurzDe2", "hurzEn2"));
    mockMvc.perform(put(API_SURVEY_URI + "/" + survey.getId())
      .content(TestUtil.convertObjectToJsonBytes(survey)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isNoContent());

    survey.setVersion(1L);
    // update the dataPackage again with the given id
    survey.setTitle(new I18nString("hurzDe3", "hurzEn3"));
    mockMvc.perform(put(API_SURVEY_URI + "/" + survey.getId())
      .content(TestUtil.convertObjectToJsonBytes(survey)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isNoContent());

    // read the dataPackage versions
    mockMvc.perform(get(API_SURVEY_URI + "/" + survey.getId() + "/versions"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.length()", is(equalTo(3))))
      .andExpect(jsonPath("$[0].id", is(survey.getId())))
      .andExpect(jsonPath("$[0].title.de", is("hurzDe3")))
      .andExpect(jsonPath("$[0].version", is(equalTo(2))))
      .andExpect(jsonPath("$[1].id", is(survey.getId())))
      .andExpect(jsonPath("$[1].title.de", is("hurzDe2")))
      .andExpect(jsonPath("$[1].version", is(equalTo(1))))
      .andExpect(jsonPath("$[2].id", is(survey.getId())))
      .andExpect(jsonPath("$[2].version", is(equalTo(0))))
      .andExpect(jsonPath("$[2].title.de", is(firstTitle)));
  }

  @Test
  public void testSurveyVersionsNotFound() throws IOException, Exception {
    mockMvc.perform(get(API_SURVEY_URI + "/spaß/versions")).andExpect(status().isOk())
        .andExpect(jsonPath("$.length()", is(0)));
  }
}

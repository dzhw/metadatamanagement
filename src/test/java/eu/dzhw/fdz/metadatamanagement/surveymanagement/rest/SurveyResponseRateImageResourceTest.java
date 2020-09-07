package eu.dzhw.fdz.metadatamanagement.surveymanagement.rest;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.rest.TestUtil;
import eu.dzhw.fdz.metadatamanagement.common.service.GridFsMetadataUpdateService;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.SurveyResponseRateImageMetadata;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

public class SurveyResponseRateImageResourceTest extends AbstractTest {

  private static final String PROJECT_ID = "issue1991";

  private static final String SURVEY_ID = "sur-issue1991-sy1$";

  private static final String FILE_NAME = "1_responserate_en";

  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private SurveyRepository surveyRepository;

  @Autowired
  private ElasticsearchUpdateQueueItemRepository elasticsearchUpdateQueueItemRepository;
  
  @Autowired
  private ElasticsearchAdminService elasticsearchAdminService;

  @Autowired
  private JaversService javersService;

  @Autowired
  private GridFsOperations gridFsOperations;

  @Autowired
  private GridFsMetadataUpdateService gridFsMetadataUpdateService;

  private MockMvc mockMvc;

  @Before
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
        .build();
  }

  @After
  public void cleanUp() {
    this.surveyRepository.deleteAll();
    this.elasticsearchUpdateQueueItemRepository.deleteAll();
    this.elasticsearchAdminService.recreateAllIndices();
    javersService.deleteAll();
    this.gridFsOperations.delete(new Query());
  }

  @Test
  @WithMockUser(authorities= AuthoritiesConstants.PUBLISHER)
  public void testCreateSurveyResponseRateImageMetadata() throws Exception {
    MockMultipartFile attachment =
        new MockMultipartFile("image", FILE_NAME, "image/png", "fakeimage".getBytes());
    SurveyResponseRateImageMetadata surveyResponseRateImageMetadata = createResponseRateImageMetadata();
    MockMultipartFile metadata = new MockMultipartFile("surveyResponseRateImageMetadata", FILE_NAME,
        "application/json", TestUtil.convertObjectToJsonBytes(surveyResponseRateImageMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/surveys/images")
        .file(attachment)
        .file(metadata))
        .andExpect(status().isCreated());
  }

  @Test
  @WithMockUser(authorities= AuthoritiesConstants.PUBLISHER)
  public void testCreateShadowCopySurveyResponseRateImageMetadata() throws Exception {
    MockMultipartFile attachment =
        new MockMultipartFile("image", FILE_NAME, "image/png", "fakeimage".getBytes());
    SurveyResponseRateImageMetadata surveyResponseRateImageMetadata = createResponseRateImageMetadata();
    surveyResponseRateImageMetadata.setSurveyId(surveyResponseRateImageMetadata.getSurveyId() + "-1.0.0");
    surveyResponseRateImageMetadata.generateId();
    MockMultipartFile metadata = new MockMultipartFile("surveyResponseRateImageMetadata", FILE_NAME,
        "application/json", TestUtil.convertObjectToJsonBytes(surveyResponseRateImageMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/surveys/images")
        .file(attachment)
        .file(metadata))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].message", containsString("global.error.shadow-create-not-allowed")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testDeleteAllResponseRateImagesOfSurvey() throws Exception {
    SurveyResponseRateImageMetadata metadata = createResponseRateImageMetadata();
    createTestFileForSurveyRateImage(metadata);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(PROJECT_ID);
    survey.setId(survey.getId() + "-1.0.0");
    surveyRepository.save(survey);

    mockMvc.perform(delete("/api/surveys/" + SURVEY_ID + "-1.0.0/images"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].message", containsString("global.error.shadow-delete-not-allowed")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testDeleteResponseRateImageOfSurvey() throws Exception {
    SurveyResponseRateImageMetadata metadata = createResponseRateImageMetadata();
    createTestFileForSurveyRateImage(metadata);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(PROJECT_ID);
    survey.setId(survey.getId() + "-1.0.0");
    surveyRepository.save(survey);

    mockMvc.perform(delete("/api/surveys/" + SURVEY_ID + "-1.0.0/images/" + metadata.getFileName()))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].message", containsString("global.error.shadow-delete-not-allowed")));
  }

  private SurveyResponseRateImageMetadata createResponseRateImageMetadata() {
    SurveyResponseRateImageMetadata master = new SurveyResponseRateImageMetadata();
    master.setSurveyId(SURVEY_ID);
    master.setDataAcquisitionProjectId(PROJECT_ID);
    master.setLanguage("en");
    master.setFileName(FILE_NAME);
    master.setSurveyNumber(1);
    return master;
  }

  private void createTestFileForSurveyRateImage(SurveyResponseRateImageMetadata metadata)
      throws Exception {

    try (InputStream is = new ByteArrayInputStream("fakeimage".getBytes(StandardCharsets.UTF_8))) {
      String filename = String.format("/surveys/%s/%s", metadata.getSurveyId(),
          metadata.getFileName());
      gridFsMetadataUpdateService.store(is, filename, "image/png", metadata);
    }
  }
}
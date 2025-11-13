package eu.dzhw.fdz.metadatamanagement.surveymanagement.rest;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
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

@Disabled
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

  @BeforeEach
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
  }

  @AfterEach
  public void cleanUp() {
    this.surveyRepository.deleteAll();
    this.elasticsearchUpdateQueueItemRepository.deleteAll();
    this.elasticsearchAdminService.recreateAllIndices();
    javersService.deleteAll();
    this.gridFsOperations.delete(new Query());
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testCreateSurveyResponseRateImageMetadata() throws Exception {
    MockMultipartFile attachment =
        new MockMultipartFile("image", FILE_NAME, "image/png", "fakeimage".getBytes());
    SurveyResponseRateImageMetadata surveyResponseRateImageMetadata =
        createResponseRateImageMetadata();
    MockMultipartFile metadata = new MockMultipartFile("surveyResponseRateImageMetadata", FILE_NAME,
        "application/json", TestUtil.convertObjectToJsonBytes(surveyResponseRateImageMetadata));

    // create the image
    MvcResult result = mockMvc
        .perform(
            MockMvcRequestBuilders.multipart("/api/surveys/images").file(attachment).file(metadata))
        .andExpect(status().isCreated()).andReturn();

    // assert that the file exists
    String fileUri = result.getResponse().getHeaderValue(HttpHeaders.LOCATION).toString();
    mockMvc.perform(get(fileUri)).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testUploadEmptyImage() throws Exception {
    MockMultipartFile attachment =
        new MockMultipartFile("image", FILE_NAME, "image/png", "".getBytes());
    SurveyResponseRateImageMetadata surveyResponseRateImageMetadata =
        createResponseRateImageMetadata();
    MockMultipartFile metadata = new MockMultipartFile("surveyResponseRateImageMetadata", FILE_NAME,
        "application/json", TestUtil.convertObjectToJsonBytes(surveyResponseRateImageMetadata));

    // create the image
    mockMvc
        .perform(
            MockMvcRequestBuilders.multipart("/api/surveys/images").file(attachment).file(metadata))
        .andExpect(status().isBadRequest());
  }


  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testDeleteAllResponseRateImages() throws Exception {
    MockMultipartFile attachment =
        new MockMultipartFile("image", FILE_NAME, "image/png", "fakeimage".getBytes());
    SurveyResponseRateImageMetadata surveyResponseRateImageMetadata =
        createResponseRateImageMetadata();
    MockMultipartFile metadata = new MockMultipartFile("surveyResponseRateImageMetadata", FILE_NAME,
        "application/json", TestUtil.convertObjectToJsonBytes(surveyResponseRateImageMetadata));

    // create the image
    MvcResult result = mockMvc
        .perform(
            MockMvcRequestBuilders.multipart("/api/surveys/images").file(attachment).file(metadata))
        .andExpect(status().isCreated()).andReturn();

    // assert that the file exists
    String fileUri = result.getResponse().getHeaderValue(HttpHeaders.LOCATION).toString();
    mockMvc.perform(get(fileUri)).andExpect(status().isOk());

    // delete all images
    mockMvc
        .perform(
            delete("/api/surveys/" + surveyResponseRateImageMetadata.getSurveyId() + "/images"))
        .andExpect(status().isNoContent());

    // assert that the file does not exist anymore
    mockMvc.perform(get(fileUri)).andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testDeleteSingleResponseRateImage() throws Exception {
    MockMultipartFile attachment =
        new MockMultipartFile("image", FILE_NAME, "image/png", "fakeimage".getBytes());
    SurveyResponseRateImageMetadata surveyResponseRateImageMetadata =
        createResponseRateImageMetadata();
    MockMultipartFile metadata = new MockMultipartFile("surveyResponseRateImageMetadata", FILE_NAME,
        "application/json", TestUtil.convertObjectToJsonBytes(surveyResponseRateImageMetadata));

    // create the image
    MvcResult result = mockMvc
        .perform(
            MockMvcRequestBuilders.multipart("/api/surveys/images").file(attachment).file(metadata))
        .andExpect(status().isCreated()).andReturn();

    // assert that the file exists
    String fileUri = result.getResponse().getHeaderValue(HttpHeaders.LOCATION).toString();
    mockMvc.perform(get(fileUri)).andExpect(status().isOk());

    // delete the image
    mockMvc.perform(delete("/api/surveys/" + surveyResponseRateImageMetadata.getSurveyId()
        + "/images/" + surveyResponseRateImageMetadata.getFileName()))
        .andExpect(status().isNoContent());

    // assert that the file does not exist anymore
    mockMvc.perform(get(fileUri)).andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testCreateShadowCopySurveyResponseRateImageMetadata() throws Exception {
    MockMultipartFile attachment =
        new MockMultipartFile("image", FILE_NAME, "image/png", "fakeimage".getBytes());
    SurveyResponseRateImageMetadata surveyResponseRateImageMetadata =
        createResponseRateImageMetadata();
    surveyResponseRateImageMetadata
        .setSurveyId(surveyResponseRateImageMetadata.getSurveyId() + "-1.0.0");
    surveyResponseRateImageMetadata.generateId();
    MockMultipartFile metadata = new MockMultipartFile("surveyResponseRateImageMetadata", FILE_NAME,
        "application/json", TestUtil.convertObjectToJsonBytes(surveyResponseRateImageMetadata));

    mockMvc
        .perform(
            MockMvcRequestBuilders.multipart("/api/surveys/images").file(attachment).file(metadata))
        .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors[0].message",
            containsString("global.error.shadow-create-not-allowed")));
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
        .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors[0].message",
            containsString("global.error.shadow-delete-not-allowed")));
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
        .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors[0].message",
            containsString("global.error.shadow-delete-not-allowed")));
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
      String filename =
          String.format("/surveys/%s/%s", metadata.getSurveyId(), metadata.getFileName());
      gridFsMetadataUpdateService.store(is, filename, "image/png", metadata);
    }
  }
}

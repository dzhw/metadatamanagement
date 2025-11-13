package eu.dzhw.fdz.metadatamanagement.surveymanagement.rest;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.rest.TestUtil;
import eu.dzhw.fdz.metadatamanagement.common.service.GridFsMetadataUpdateService;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.SurveyAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.service.SurveyAttachmentService;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.service.helper.SurveyAttachmentFilenameBuilder;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

@Disabled
public class SurveyAttachmentResourceTest extends AbstractTest {
  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private SurveyRepository surveyRepository;

  @Autowired
  private SurveyAttachmentService surveyAttachmentService;

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
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
      .build();
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
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER, username="test")
  public void testUploadValidAttachment() throws Exception {
    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    SurveyAttachmentMetadata surveyAttachmentMetadata = UnitTestCreateDomainObjectUtils
      .buildSurveyAttachmentMetadata("projectid", 1);
    MockMultipartFile metadata = new MockMultipartFile("surveyAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(surveyAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/surveys/attachments")
      .file(attachment)
      .file(metadata))
      .andExpect(status().isCreated());

    // read the created attachment and check the version
    mockMvc.perform(
        get("/api/surveys/" + surveyAttachmentMetadata.getSurveyId() + "/attachments"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.[0].surveyId", is(surveyAttachmentMetadata.getSurveyId())))
      .andExpect(jsonPath("$.[0].version", is(0)))
      .andExpect(jsonPath("$.[0].createdBy", is("test")))
      .andExpect(jsonPath("$.[0].lastModifiedBy", is("test")))
      .andExpect(jsonPath("$.[0].masterId", is("/public/files/surveys/"
          + "sur-projectid-sy1$/attachments/filename.txt")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER, username = "test")
  public void testDeleteSingleAttachment() throws Exception {
    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    SurveyAttachmentMetadata surveyAttachmentMetadata =
        UnitTestCreateDomainObjectUtils.buildSurveyAttachmentMetadata("projectid", 1);
    MockMultipartFile metadata = new MockMultipartFile("surveyAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(surveyAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/surveys/attachments").file(attachment)
        .file(metadata)).andExpect(status().isCreated());

    // ensure that there is one attachment
    mockMvc.perform(get("/api/surveys/" + surveyAttachmentMetadata.getSurveyId() + "/attachments"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(1)));;

    // delete the file
    mockMvc.perform(delete("/api/surveys/" + surveyAttachmentMetadata.getSurveyId()
        + "/attachments/" + attachment.getOriginalFilename()))
        .andExpect(status().is2xxSuccessful());

    // ensure the uploaded file does not exist anymore
    mockMvc.perform(get("/api/surveys/" + surveyAttachmentMetadata.getSurveyId() + "/attachments"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(0)));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER, username = "test")
  public void testDeleteAllAttachments() throws Exception {
    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    SurveyAttachmentMetadata surveyAttachmentMetadata =
        UnitTestCreateDomainObjectUtils.buildSurveyAttachmentMetadata("projectid", 1);
    MockMultipartFile metadata = new MockMultipartFile("surveyAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(surveyAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/surveys/attachments").file(attachment)
        .file(metadata)).andExpect(status().isCreated());

    // ensure that there is one attachment
    mockMvc.perform(get("/api/surveys/" + surveyAttachmentMetadata.getSurveyId() + "/attachments"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(1)));;

    // delete all file
    mockMvc
        .perform(delete("/api/surveys/" + surveyAttachmentMetadata.getSurveyId() + "/attachments"))
        .andExpect(status().is2xxSuccessful());

    // ensure the uploaded file does not exist anymore
    mockMvc.perform(get("/api/surveys/" + surveyAttachmentMetadata.getSurveyId() + "/attachments"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(0)));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER, username = "test")
  public void testUpdateAttachmentMetadata() throws Exception {
    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    SurveyAttachmentMetadata surveyAttachmentMetadata =
        UnitTestCreateDomainObjectUtils.buildSurveyAttachmentMetadata("projectid", 1);
    MockMultipartFile metadata = new MockMultipartFile("surveyAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(surveyAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/surveys/attachments").file(attachment)
        .file(metadata)).andExpect(status().isCreated());

    // ensure that there is one attachment
    mockMvc.perform(get("/api/surveys/" + surveyAttachmentMetadata.getSurveyId() + "/attachments"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(1)));;

    // update the metadata
    SurveyAttachmentMetadata current =
        surveyAttachmentService.findAllBySurvey(surveyAttachmentMetadata.getSurveyId()).get(0);
    current.setLanguage("en");

    mockMvc
        .perform(put("/api/surveys/" + surveyAttachmentMetadata.getSurveyId() + "/attachments/"
            + attachment.getOriginalFilename()).content(TestUtil.convertObjectToJsonBytes(current))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());

    // read the updated attachment and check the version
    mockMvc
        .perform(get("/api/surveys/" + surveyAttachmentMetadata.getSurveyId() + "/attachments"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.[0].version", is(1)))
        .andExpect(jsonPath("$.[0].createdBy", is("test")))
        .andExpect(jsonPath("$.[0].lastModifiedBy", is("test")))
        .andExpect(jsonPath("$.[0].language", is("en")));
  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testUploadAttachmentWithWrongLanguage() throws Exception {

    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    SurveyAttachmentMetadata surveyAttachmentMetadata = UnitTestCreateDomainObjectUtils
      .buildSurveyAttachmentMetadata("projectid", 1);
    surveyAttachmentMetadata.setLanguage("test");
    MockMultipartFile metadata = new MockMultipartFile("surveyAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(surveyAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/surveys/attachments")
      .file(attachment)
      .file(metadata))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.errors[0].message",
          is("survey-management.error.survey-attachment-metadata.language.not-supported")));
  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testUploadAttachmentWithMissingDescription() throws Exception {

    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    SurveyAttachmentMetadata surveyAttachmentMetadata = UnitTestCreateDomainObjectUtils
      .buildSurveyAttachmentMetadata("projectid", 1);
    surveyAttachmentMetadata.setDescription(new I18nString());

    MockMultipartFile metadata = new MockMultipartFile("surveyAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(surveyAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/surveys/attachments")
      .file(attachment)
      .file(metadata))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.errors[0].message",
          is("survey-management.error.survey-attachment-metadata.description.i18n-string-not-empty")));
  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testUploadAttachmentWithMissingDOI() throws Exception {

    MockMultipartFile attachment =
      new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    SurveyAttachmentMetadata surveyAttachmentMetadata = UnitTestCreateDomainObjectUtils
      .buildSurveyAttachmentMetadata("projectid", 1);
    surveyAttachmentMetadata.setDoi(null);

    MockMultipartFile metadata = new MockMultipartFile("surveyAttachmentMetadata", "Blob",
      "application/json", TestUtil.convertObjectToJsonBytes(surveyAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/surveys/attachments")
      .file(attachment)
      .file(metadata))
      .andExpect(status().isCreated());
  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testUploadAttachmentWithValidDOI() throws Exception {

    MockMultipartFile attachment =
      new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    SurveyAttachmentMetadata surveyAttachmentMetadata = UnitTestCreateDomainObjectUtils
      .buildSurveyAttachmentMetadata("projectid", 1);
    surveyAttachmentMetadata.setDoi("https://doi.org/1");

    MockMultipartFile metadata = new MockMultipartFile("surveyAttachmentMetadata", "Blob",
      "application/json", TestUtil.convertObjectToJsonBytes(surveyAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/surveys/attachments")
      .file(attachment)
      .file(metadata))
      .andExpect(status().isCreated());
  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testUploadAttachmentWithInvalidDOI() throws Exception {

    MockMultipartFile attachment =
      new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    SurveyAttachmentMetadata surveyAttachmentMetadata = UnitTestCreateDomainObjectUtils
      .buildSurveyAttachmentMetadata("projectid", 1);
    surveyAttachmentMetadata.setDoi("https://invalid.org/1");

    MockMultipartFile metadata = new MockMultipartFile("surveyAttachmentMetadata", "Blob",
      "application/json", TestUtil.convertObjectToJsonBytes(surveyAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/surveys/attachments")
      .file(attachment)
      .file(metadata))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.errors[0].message",
        is("attachment.error.doi.pattern")));
  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testAttachmentIsDeletedWithSurvey() throws Exception {
    Survey survey =
        UnitTestCreateDomainObjectUtils.buildSurvey("projectid");

    // create the survey with the given id
    mockMvc.perform(put("/api/surveys/" + survey.getId())
      .content(TestUtil.convertObjectToJsonBytes(survey)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated());

    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    SurveyAttachmentMetadata surveyAttachmentMetadata = UnitTestCreateDomainObjectUtils
      .buildSurveyAttachmentMetadata(survey.getDataAcquisitionProjectId(), survey.getNumber());

    MockMultipartFile metadata = new MockMultipartFile("surveyAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(surveyAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/surveys/attachments")
      .file(attachment)
      .file(metadata))
      .andExpect(status().isCreated());

    // delete the survey with the given id
    mockMvc.perform(delete("/api/surveys/" + survey.getId()))
      .andExpect(status().isNoContent());

    // check if attachment has been deleted as well
    mockMvc.perform(
        get("/api/surveys/" + surveyAttachmentMetadata.getSurveyId() + "/attachments"))
      .andExpect(status().isOk())
      .andExpect(content().json("[]"));
  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testCreateShadowCopySurveyAttachment() throws Exception {
    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    SurveyAttachmentMetadata surveyAttachmentMetadata = UnitTestCreateDomainObjectUtils
        .buildSurveyAttachmentMetadata("projectid", 1);
    surveyAttachmentMetadata.setSurveyId(surveyAttachmentMetadata.getSurveyId() + "-1.0.0");
    surveyAttachmentMetadata.generateId();
    MockMultipartFile metadata = new MockMultipartFile("surveyAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(surveyAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/surveys/attachments")
        .file(attachment)
        .file(metadata))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].message", containsString("global.error.shadow-create-not-allowed")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testUpdateAttachmentOfShadowCopySurvey() throws Exception {
    String surveyId = "sur-issue1991-sy1$-1.0.0";
    SurveyAttachmentMetadata metadata = UnitTestCreateDomainObjectUtils
        .buildSurveyAttachmentMetadata("issue1991", 1);
    metadata.setSurveyId(surveyId);
    metadata.generateId();
    metadata.setVersion(0L);

    String filename = SurveyAttachmentFilenameBuilder.buildFileName(metadata);
    try (InputStream is = new ByteArrayInputStream("Test".getBytes(StandardCharsets.UTF_8))) {
      gridFsMetadataUpdateService.store(is, filename, "text/plain", metadata);
    }

    mockMvc.perform(put("/api/surveys/" + surveyId + "/attachments/" + metadata.getFileName())
        .content(TestUtil.convertObjectToJsonBytes(metadata))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].message", containsString("global.error.shadow-save-not-allowed")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testDeleteAllAttachmentsOfShadowCopyDataPackage() throws Exception {
    String surveyId = "sur-issue1991-sy1$-1.0.0";

    SurveyAttachmentMetadata metadata = UnitTestCreateDomainObjectUtils
        .buildSurveyAttachmentMetadata("issue1991", 1);
    metadata.setSurveyId(surveyId);
    metadata.generateId();

    try (InputStream is = new ByteArrayInputStream("Test".getBytes(StandardCharsets.UTF_8))) {
      String filename = SurveyAttachmentFilenameBuilder.buildFileName(metadata);
      gridFsMetadataUpdateService.store(is, filename, "text/plain", metadata);
    }

    mockMvc.perform(delete("/api/surveys/" + surveyId + "/attachments"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].message", containsString("global.error.shadow-delete-not-allowed")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testDeleteAttachmentOfShadowCopyDataPackage() throws Exception {
    String surveyId = "sur-issue1991-sy1$-1.0.0";

    SurveyAttachmentMetadata metadata = UnitTestCreateDomainObjectUtils
        .buildSurveyAttachmentMetadata("issue1991", 1);
    metadata.setSurveyId(surveyId);
    metadata.generateId();

    String filename = SurveyAttachmentFilenameBuilder.buildFileName(metadata);
    try (InputStream is = new ByteArrayInputStream("Test".getBytes(StandardCharsets.UTF_8))) {
      gridFsMetadataUpdateService.store(is, filename, "text/plain", metadata);
    }

    mockMvc.perform(delete("/api/surveys/" + surveyId + "/attachments/" + metadata.getFileName()))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].message", containsString("global.error.shadow-delete-not-allowed")));
  }

}

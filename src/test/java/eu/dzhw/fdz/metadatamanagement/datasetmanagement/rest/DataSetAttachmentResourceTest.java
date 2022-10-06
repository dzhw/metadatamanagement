package eu.dzhw.fdz.metadatamanagement.datasetmanagement.rest;

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
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSetAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.service.DataSetAttachmentService;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.service.helper.DataSetAttachmentFilenameBuilder;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

public class DataSetAttachmentResourceTest extends AbstractTest {
  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private DataSetRepository dataSetRepository;

  @Autowired
  private DataSetAttachmentService dataSetAttachmentService;

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
    this.dataSetRepository.deleteAll();
    this.elasticsearchUpdateQueueItemRepository.deleteAll();
    this.elasticsearchAdminService.recreateAllIndices();
    this.javersService.deleteAll();
    this.gridFsOperations.delete(new Query());
  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER, username="test")
  public void testUploadValidAttachment() throws Exception {

    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    DataSetAttachmentMetadata dataSetAttachmentMetadata = UnitTestCreateDomainObjectUtils
      .buildDataSetAttachmentMetadata("projectid", 1);
    MockMultipartFile metadata = new MockMultipartFile("dataSetAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(dataSetAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/data-sets/attachments")
      .file(attachment)
      .file(metadata))
      .andExpect(status().isCreated());

    dataSetAttachmentMetadata.generateId();

    // read the created attachment and check the version
    mockMvc.perform(
        get("/api/data-sets/" + dataSetAttachmentMetadata.getDataSetId() + "/attachments"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.[0].dataSetId", is(dataSetAttachmentMetadata.getDataSetId())))
      .andExpect(jsonPath("$.[0].version", is(0)))
      .andExpect(jsonPath("$.[0].createdBy", is("test")))
      .andExpect(jsonPath("$.[0].lastModifiedBy", is("test")))
      .andExpect(jsonPath("$.[0].masterId", is(dataSetAttachmentMetadata.getId())));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER, username = "test")
  public void testDeleteSingleAttachment() throws Exception {

    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    DataSetAttachmentMetadata dataSetAttachmentMetadata =
        UnitTestCreateDomainObjectUtils.buildDataSetAttachmentMetadata("projectid", 1);
    MockMultipartFile metadata = new MockMultipartFile("dataSetAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(dataSetAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/data-sets/attachments").file(attachment)
        .file(metadata)).andExpect(status().isCreated());

    dataSetAttachmentMetadata.generateId();

    // ensure that there is one attachment
    mockMvc
        .perform(get("/api/data-sets/" + dataSetAttachmentMetadata.getDataSetId() + "/attachments"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(1)));;

    // delete the file
    mockMvc
        .perform(delete("/api/data-sets/" + dataSetAttachmentMetadata.getDataSetId()
            + "/attachments/" + attachment.getOriginalFilename()))
        .andExpect(status().is2xxSuccessful());

    // ensure the uploaded file does not exist anymore
    mockMvc
        .perform(get("/api/data-sets/" + dataSetAttachmentMetadata.getDataSetId() + "/attachments"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(0)));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER, username = "test")
  public void testDeleteAllAttachments() throws Exception {

    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    DataSetAttachmentMetadata dataSetAttachmentMetadata =
        UnitTestCreateDomainObjectUtils.buildDataSetAttachmentMetadata("projectid", 1);
    MockMultipartFile metadata = new MockMultipartFile("dataSetAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(dataSetAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/data-sets/attachments").file(attachment)
        .file(metadata)).andExpect(status().isCreated());

    dataSetAttachmentMetadata.generateId();

    // ensure that there is one attachment
    mockMvc
        .perform(get("/api/data-sets/" + dataSetAttachmentMetadata.getDataSetId() + "/attachments"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(1)));;

    // delete all files
    mockMvc
        .perform(delete("/api/data-sets/" + dataSetAttachmentMetadata.getDataSetId()
            + "/attachments"))
        .andExpect(status().is2xxSuccessful());

    // ensure the uploaded file does not exist anymore
    mockMvc
        .perform(get("/api/data-sets/" + dataSetAttachmentMetadata.getDataSetId() + "/attachments"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(0)));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER, username = "test")
  public void testUpdateAttachment() throws Exception {
    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    DataSetAttachmentMetadata dataSetAttachmentMetadata =
        UnitTestCreateDomainObjectUtils.buildDataSetAttachmentMetadata("projectid", 1);
    MockMultipartFile metadata = new MockMultipartFile("dataSetAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(dataSetAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/data-sets/attachments").file(attachment)
        .file(metadata)).andExpect(status().isCreated());

    dataSetAttachmentMetadata.generateId();

    // ensure that there is one attachment
    mockMvc
        .perform(get("/api/data-sets/" + dataSetAttachmentMetadata.getDataSetId() + "/attachments"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(1)));;

    // update the metadata
    DataSetAttachmentMetadata current =
        dataSetAttachmentService.findAllByDataSet(dataSetAttachmentMetadata.getDataSetId()).get(0);
    current.setLanguage("en");

    mockMvc
        .perform(put("/api/data-sets/" + dataSetAttachmentMetadata.getDataSetId() + "/attachments/"
            + attachment.getOriginalFilename()).content(TestUtil.convertObjectToJsonBytes(current))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());

    // read the updated attachment and check the version
    mockMvc
        .perform(get(
            "/api/data-sets/" + dataSetAttachmentMetadata.getDataSetId() + "/attachments"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[0].version", is(1)))
        .andExpect(jsonPath("$.[0].createdBy", is("test")))
        .andExpect(jsonPath("$.[0].lastModifiedBy", is("test")))
        .andExpect(jsonPath("$.[0].language", is("en")));
  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testUploadAttachmentWithWrongLanguage() throws Exception {

    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    DataSetAttachmentMetadata dataSetAttachmentMetadata = UnitTestCreateDomainObjectUtils
      .buildDataSetAttachmentMetadata("projectid", 1);
    dataSetAttachmentMetadata.setLanguage("test");
    MockMultipartFile metadata = new MockMultipartFile("dataSetAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(dataSetAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/data-sets/attachments")
      .file(attachment)
      .file(metadata))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.errors[0].message",
          is("data-set-management.error.data-set-attachment-metadata.language.not-supported")));
  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testUploadAttachmentWithMissingDescription() throws Exception {

    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    DataSetAttachmentMetadata dataSetAttachmentMetadata = UnitTestCreateDomainObjectUtils
      .buildDataSetAttachmentMetadata("projectid", 1);
    dataSetAttachmentMetadata.setDescription(new I18nString());

    MockMultipartFile metadata = new MockMultipartFile("dataSetAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(dataSetAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/data-sets/attachments")
      .file(attachment)
      .file(metadata))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.errors[0].message",
          is("data-set-management.error.data-set-attachment-metadata.description.i18n-string-not-empty")));
  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testUploadAttachmentWithMissingDOI() throws Exception {

    MockMultipartFile attachment =
      new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    DataSetAttachmentMetadata dataSetAttachmentMetadata = UnitTestCreateDomainObjectUtils
      .buildDataSetAttachmentMetadata("projectid", 1);
    dataSetAttachmentMetadata.setDoi(null);

    MockMultipartFile metadata = new MockMultipartFile("dataSetAttachmentMetadata", "Blob",
      "application/json", TestUtil.convertObjectToJsonBytes(dataSetAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/data-sets/attachments")
      .file(attachment)
      .file(metadata))
      .andExpect(status().isCreated());
  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testUploadAttachmentWithValidDOI() throws Exception {

    MockMultipartFile attachment =
      new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    DataSetAttachmentMetadata dataSetAttachmentMetadata = UnitTestCreateDomainObjectUtils
      .buildDataSetAttachmentMetadata("projectid", 1);
    dataSetAttachmentMetadata.setDoi("https://doi.org/1");

    MockMultipartFile metadata = new MockMultipartFile("dataSetAttachmentMetadata", "Blob",
      "application/json", TestUtil.convertObjectToJsonBytes(dataSetAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/data-sets/attachments")
      .file(attachment)
      .file(metadata))
      .andExpect(status().isCreated());
  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testUploadAttachmentWithInvalidDOI() throws Exception {

    MockMultipartFile attachment =
      new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    DataSetAttachmentMetadata dataSetAttachmentMetadata = UnitTestCreateDomainObjectUtils
      .buildDataSetAttachmentMetadata("projectid", 1);
    dataSetAttachmentMetadata.setDoi("https://invalid.org/1");

    MockMultipartFile metadata = new MockMultipartFile("dataSetAttachmentMetadata", "Blob",
      "application/json", TestUtil.convertObjectToJsonBytes(dataSetAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/data-sets/attachments")
      .file(attachment)
      .file(metadata))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.errors[0].message", is("data-set-management.error.data-set-attachment-metadata.filename.not-valid")));
  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testAttachmentIsDeletedWithDataSet() throws Exception {
    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey("projectId");
    DataSet dataSet =
        UnitTestCreateDomainObjectUtils.buildDataSet("projectid", survey.getId() , survey.getNumber());

    // create the dataSet with the given id
    mockMvc.perform(put("/api/data-sets/" + dataSet.getId())
      .content(TestUtil.convertObjectToJsonBytes(dataSet)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated());

    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    DataSetAttachmentMetadata dataSetAttachmentMetadata = UnitTestCreateDomainObjectUtils
      .buildDataSetAttachmentMetadata(dataSet.getDataAcquisitionProjectId(), dataSet.getNumber());

    MockMultipartFile metadata = new MockMultipartFile("dataSetAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(dataSetAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/data-sets/attachments")
      .file(attachment)
      .file(metadata))
      .andExpect(status().isCreated());

    // delete the dataSet with the given id
    mockMvc.perform(delete("/api/data-sets/" + dataSet.getId()))
      .andExpect(status().isNoContent());

    // check if attachment has been deleted as well
    mockMvc.perform(
        get("/api/data-sets/" + dataSetAttachmentMetadata.getDataSetId() + "/attachments"))
      .andExpect(status().isOk())
      .andExpect(content().json("[]"));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void createShadowCopyDataSetAttachment() throws Exception {
    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    DataSetAttachmentMetadata dataSetAttachmentMetadata = UnitTestCreateDomainObjectUtils
        .buildDataSetAttachmentMetadata("issue1991", 1);
    dataSetAttachmentMetadata.setDataSetId(dataSetAttachmentMetadata.getDataSetId() + "-1.0.0");
    dataSetAttachmentMetadata.generateId();

    MockMultipartFile metadata = new MockMultipartFile("dataSetAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(dataSetAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/data-sets/attachments")
        .file(attachment)
        .file(metadata))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].message", containsString("global.error.shadow-create-not-allowed")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testUpdateAttachmentOfShadowDataSet() throws Exception {
    String dataSetId = "dat-issue1991-ds1$-1.0.0";
    DataSetAttachmentMetadata metadata = UnitTestCreateDomainObjectUtils
        .buildDataSetAttachmentMetadata("issue1991", 1);
    metadata.setDataSetId(dataSetId);
    metadata.generateId();
    metadata.setVersion(0L);

    String filename = DataSetAttachmentFilenameBuilder.buildFileName(metadata);
    try (InputStream is = new ByteArrayInputStream("Test".getBytes(StandardCharsets.UTF_8))) {
      gridFsMetadataUpdateService.store(is, filename, "text/plain", metadata);
    }

    mockMvc.perform(put("/api/data-sets/" + dataSetId + "/attachments/" + metadata.getFileName())
        .content(TestUtil.convertObjectToJsonBytes(metadata))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].message", containsString("global.error.shadow-save-not-allowed")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testDeleteAllAttachmentsOfShadowCopyDataSet() throws Exception {
    String dataSetId = "dat-issue1991-ds1$-1.0.0";

    DataSetAttachmentMetadata metadata = UnitTestCreateDomainObjectUtils
        .buildDataSetAttachmentMetadata("issue1991", 1);
    metadata.setDataSetId(dataSetId);
    metadata.generateId();

    try (InputStream is = new ByteArrayInputStream("Test".getBytes(StandardCharsets.UTF_8))) {
      String filename = DataSetAttachmentFilenameBuilder.buildFileName(metadata);
      gridFsMetadataUpdateService.store(is, filename, "text/plain", metadata);
    }

    mockMvc.perform(delete("/api/data-sets/" + dataSetId + "/attachments"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].message", containsString("global.error.shadow-delete-not-allowed")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testDeleteAttachmentOfShadowDataSet() throws Exception {
    String dataSetId = "dat-issue1991-ds1$-1.0.0";

    DataSetAttachmentMetadata metadata = UnitTestCreateDomainObjectUtils
        .buildDataSetAttachmentMetadata("issue1991", 1);
    metadata.setDataSetId(dataSetId);
    metadata.generateId();

    String filename = DataSetAttachmentFilenameBuilder.buildFileName(metadata);
    try (InputStream is = new ByteArrayInputStream("Test".getBytes(StandardCharsets.UTF_8))) {
      gridFsMetadataUpdateService.store(is, filename, "text/plain", metadata);
    }

    mockMvc.perform(delete("/api/data-sets/" + dataSetId + "/attachments/" + metadata.getFileName()))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].message", containsString("global.error.shadow-delete-not-allowed")));
  }
}

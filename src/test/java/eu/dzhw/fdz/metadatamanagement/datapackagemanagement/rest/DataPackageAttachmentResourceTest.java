package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.rest;

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
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackageAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackageAttachmentTypes;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.repository.DataPackageRepository;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.service.DataPackageAttachmentService;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.service.helper.DataPackageAttachmentFilenameBuilder;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

@Disabled
public class DataPackageAttachmentResourceTest extends AbstractTest {
  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private DataPackageRepository dataPackageRepository;

  @Autowired
  private ElasticsearchUpdateQueueItemRepository elasticsearchUpdateQueueItemRepository;

  @Autowired
  private DataPackageAttachmentService dataPackageAttachmentService;

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
    this.dataPackageRepository.deleteAll();
    this.elasticsearchUpdateQueueItemRepository.deleteAll();
    this.elasticsearchAdminService.recreateAllIndices();
    this.javersService.deleteAll();
    this.gridFsOperations.delete(new Query());
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER, username = "test")
  public void testUploadValidAttachment() throws Exception {

    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    DataPackageAttachmentMetadata dataPackageAttachmentMetadata =
        UnitTestCreateDomainObjectUtils.buildDataPackageAttachmentMetadata("projectid");
    MockMultipartFile metadata = new MockMultipartFile("dataPackageAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(dataPackageAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/data-packages/attachments")
        .file(attachment).file(metadata)).andExpect(status().isCreated());

    dataPackageAttachmentMetadata.generateId();

    // read the created attachment and check the version
    mockMvc
        .perform(get("/api/data-packages/"
            + dataPackageAttachmentMetadata.getDataPackageId() + "/attachments"))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath("$.[0].dataPackageId", is(dataPackageAttachmentMetadata.getDataPackageId())))
        .andExpect(jsonPath("$.[0].version", is(0)))
        .andExpect(jsonPath("$.[0].createdBy", is("test")))
        .andExpect(jsonPath("$.[0].lastModifiedBy", is("test")))
        .andExpect(jsonPath("$.[0].masterId",
            is("/public/files/data-packages/" + "stu-projectid$/attachments/filename.txt")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER, username = "test")
  public void testDeleteAttachment() throws Exception {
    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    DataPackageAttachmentMetadata dataPackageAttachmentMetadata =
        UnitTestCreateDomainObjectUtils.buildDataPackageAttachmentMetadata("projectid");
    MockMultipartFile metadata = new MockMultipartFile("dataPackageAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(dataPackageAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/data-packages/attachments")
        .file(attachment).file(metadata)).andExpect(status().isCreated());

    dataPackageAttachmentMetadata.generateId();

    // ensure that there is one attachment
    mockMvc
        .perform(get("/api/data-packages/" + dataPackageAttachmentMetadata.getDataPackageId()
            + "/attachments"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(1)));

    // delete the file
    mockMvc
        .perform(delete("/api/data-packages/" + dataPackageAttachmentMetadata.getDataPackageId()
            + "/attachments/" + attachment.getOriginalFilename()))
        .andExpect(status().is2xxSuccessful());

    // ensure the uploaded file does not exist anymore
    mockMvc.perform(get(
        "/api/data-packages/" + dataPackageAttachmentMetadata.getDataPackageId() + "/attachments"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(0)));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER, username = "test")
  public void testDeleteAllAttachments() throws Exception {
    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    DataPackageAttachmentMetadata dataPackageAttachmentMetadata =
        UnitTestCreateDomainObjectUtils.buildDataPackageAttachmentMetadata("projectid");
    MockMultipartFile metadata = new MockMultipartFile("dataPackageAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(dataPackageAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/data-packages/attachments")
        .file(attachment).file(metadata)).andExpect(status().isCreated());

    dataPackageAttachmentMetadata.generateId();

    // ensure that there is one attachment
    mockMvc
        .perform(get("/api/data-packages/" + dataPackageAttachmentMetadata.getDataPackageId()
            + "/attachments"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(1)));

    // delete the files
    mockMvc
        .perform(delete("/api/data-packages/" + dataPackageAttachmentMetadata.getDataPackageId()
            + "/attachments"))
        .andExpect(status().is2xxSuccessful());

    // ensure the uploaded file does not exist anymore
    mockMvc.perform(get(
        "/api/data-packages/" + dataPackageAttachmentMetadata.getDataPackageId() + "/attachments"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(0)));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER, username = "test")
  public void testUpdateAttachment() throws Exception {
    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    DataPackageAttachmentMetadata dataPackageAttachmentMetadata =
        UnitTestCreateDomainObjectUtils.buildDataPackageAttachmentMetadata("projectid");
    MockMultipartFile metadata = new MockMultipartFile("dataPackageAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(dataPackageAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/data-packages/attachments")
        .file(attachment).file(metadata)).andExpect(status().isCreated());

    dataPackageAttachmentMetadata.generateId();

    // ensure that there is one attachment
    mockMvc.perform(get(
        "/api/data-packages/" + dataPackageAttachmentMetadata.getDataPackageId() + "/attachments"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(1)));

    DataPackageAttachmentMetadata current = dataPackageAttachmentService
        .findAllByDataPackage(dataPackageAttachmentMetadata.getDataPackageId()).get(0);
    current.setType(DataPackageAttachmentTypes.OTHER);

    // update the metadata
    mockMvc.perform(put("/api/data-packages/" + dataPackageAttachmentMetadata.getDataPackageId()
        + "/attachments/" + attachment.getOriginalFilename())
            .content(TestUtil.convertObjectToJsonBytes(current))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful());

    // read the created attachment and check the version
    mockMvc
        .perform(get("/api/data-packages/"
            + dataPackageAttachmentMetadata.getDataPackageId() + "/attachments"))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath("$.[0].dataPackageId", is(dataPackageAttachmentMetadata.getDataPackageId())))
        .andExpect(jsonPath("$.[0].version", is(1)))
        .andExpect(jsonPath("$.[0].createdBy", is("test")))
        .andExpect(jsonPath("$.[0].lastModifiedBy", is("test")))
        .andExpect(jsonPath("$.[0].masterId",
            is("/public/files/data-packages/" + "stu-projectid$/attachments/filename.txt")))
        .andExpect(jsonPath("$.[0].type.en", is(DataPackageAttachmentTypes.OTHER.getEn())));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testUploadAttachmentWithWrongType() throws Exception {

    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    DataPackageAttachmentMetadata dataPackageAttachmentMetadata =
        UnitTestCreateDomainObjectUtils.buildDataPackageAttachmentMetadata("projectid");
    dataPackageAttachmentMetadata.setType(new I18nString("hurz", "hurz"));
    MockMultipartFile metadata = new MockMultipartFile("dataPackageAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(dataPackageAttachmentMetadata));

    mockMvc
        .perform(MockMvcRequestBuilders.multipart("/api/data-packages/attachments").file(attachment)
            .file(metadata))
        .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors[0].message",
            is("data-package-management.error.data-package-attachment-metadata.type.valid-type")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testUploadAttachmentWithMissingDescription() throws Exception {

    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    DataPackageAttachmentMetadata dataPackageAttachmentMetadata =
        UnitTestCreateDomainObjectUtils.buildDataPackageAttachmentMetadata("projectid");
    dataPackageAttachmentMetadata.setDescription(new I18nString());

    MockMultipartFile metadata = new MockMultipartFile("dataPackageAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(dataPackageAttachmentMetadata));

    mockMvc
        .perform(MockMvcRequestBuilders.multipart("/api/data-packages/attachments").file(attachment)
            .file(metadata))
        .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors[0].message", is(
            "data-package-management.error.data-package-attachment-metadata.description.i18n-string-not-empty")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testUploadAttachmentWithMissingDOI() throws Exception {

    MockMultipartFile attachment =
      new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    DataPackageAttachmentMetadata dataPackageAttachmentMetadata =
      UnitTestCreateDomainObjectUtils.buildDataPackageAttachmentMetadata("projectid");
    dataPackageAttachmentMetadata.setDoi(null);

    MockMultipartFile metadata = new MockMultipartFile("dataPackageAttachmentMetadata", "Blob",
      "application/json", TestUtil.convertObjectToJsonBytes(dataPackageAttachmentMetadata));

    mockMvc
      .perform(MockMvcRequestBuilders.multipart("/api/data-packages/attachments").file(attachment)
        .file(metadata))
      .andExpect(status().isCreated());
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testUploadAttachmentWithValidDOI() throws Exception {

    MockMultipartFile attachment =
      new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    DataPackageAttachmentMetadata dataPackageAttachmentMetadata =
      UnitTestCreateDomainObjectUtils.buildDataPackageAttachmentMetadata("projectid");
    dataPackageAttachmentMetadata.setDoi("https://doi.org/1");

    MockMultipartFile metadata = new MockMultipartFile("dataPackageAttachmentMetadata", "Blob",
      "application/json", TestUtil.convertObjectToJsonBytes(dataPackageAttachmentMetadata));

    mockMvc
      .perform(MockMvcRequestBuilders.multipart("/api/data-packages/attachments").file(attachment)
        .file(metadata))
      .andExpect(status().isCreated());
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testUploadAttachmentWithInvalidDOI() throws Exception {

    MockMultipartFile attachment =
      new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    DataPackageAttachmentMetadata dataPackageAttachmentMetadata =
      UnitTestCreateDomainObjectUtils.buildDataPackageAttachmentMetadata("projectid");
    dataPackageAttachmentMetadata.setDoi("https://invalid.org/1");

    MockMultipartFile metadata = new MockMultipartFile("dataPackageAttachmentMetadata", "Blob",
      "application/json", TestUtil.convertObjectToJsonBytes(dataPackageAttachmentMetadata));

    mockMvc
      .perform(MockMvcRequestBuilders.multipart("/api/data-packages/attachments").file(attachment)
        .file(metadata))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.errors[0].message", is("attachment.error.doi.pattern")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testAttachmentIsDeletedWithDataPackage() throws Exception {
    DataPackage dataPackage = UnitTestCreateDomainObjectUtils.buildDataPackage("projectid");

    // create the dataPackage with the given id
    mockMvc.perform(put("/api/data-packages/" + dataPackage.getId())
        .content(TestUtil.convertObjectToJsonBytes(dataPackage))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());

    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    DataPackageAttachmentMetadata dataPackageAttachmentMetadata = UnitTestCreateDomainObjectUtils
        .buildDataPackageAttachmentMetadata(dataPackage.getDataAcquisitionProjectId());

    MockMultipartFile metadata = new MockMultipartFile("dataPackageAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(dataPackageAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/data-packages/attachments")
        .file(attachment).file(metadata)).andExpect(status().isCreated());

    // delete the dataPackage by project id
    mockMvc.perform(delete("/api/data-packages/" + dataPackage.getId()))
        .andExpect(status().isNoContent());

    // check if attachment has been deleted as well
    mockMvc.perform(get(
        "/api/data-packages/" + dataPackageAttachmentMetadata.getDataPackageId() + "/attachments"))
        .andExpect(status().isOk()).andExpect(content().json("[]"));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testCreateShadowCopyInstrumentAttachment() throws Exception {
    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    DataPackageAttachmentMetadata dataPackageAttachmentMetadata =
        UnitTestCreateDomainObjectUtils.buildDataPackageAttachmentMetadata("projectid");
    dataPackageAttachmentMetadata
        .setDataPackageId(dataPackageAttachmentMetadata.getDataPackageId() + "-1.0.0");
    dataPackageAttachmentMetadata.generateId();
    MockMultipartFile metadata = new MockMultipartFile("dataPackageAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(dataPackageAttachmentMetadata));

    mockMvc
        .perform(MockMvcRequestBuilders.multipart("/api/data-packages/attachments").file(attachment)
            .file(metadata))
        .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors[0].message",
            containsString("global.error.shadow-create-not-allowed")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testUpdateAttachmentOfShadowCopyDataPackage() throws Exception {
    String dataPackageId = "stu-issue1991-1.0.0";
    DataPackageAttachmentMetadata metadata =
        UnitTestCreateDomainObjectUtils.buildDataPackageAttachmentMetadata("issue1991");
    metadata.setDataPackageId(dataPackageId);
    metadata.generateId();
    metadata.setVersion(0L);

    String filename = DataPackageAttachmentFilenameBuilder.buildFileName(metadata);
    try (InputStream is = new ByteArrayInputStream("Test".getBytes(StandardCharsets.UTF_8))) {
      gridFsMetadataUpdateService.store(is, filename, "text/plain", metadata);
    }

    mockMvc
        .perform(
            put("/api/data-packages/" + dataPackageId + "/attachments/" + metadata.getFileName())
                .content(TestUtil.convertObjectToJsonBytes(metadata))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors[0].message",
            containsString("global.error.shadow-save-not-allowed")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testDeleteAllAttachmentsOfShadowCopyDataPackage() throws Exception {
    String dataPackageId = "stu-issue1991-1.0.0";

    DataPackageAttachmentMetadata metadata =
        UnitTestCreateDomainObjectUtils.buildDataPackageAttachmentMetadata("issue1991");
    metadata.setDataPackageId(dataPackageId);
    metadata.generateId();

    try (InputStream is = new ByteArrayInputStream("Test".getBytes(StandardCharsets.UTF_8))) {
      String filename = DataPackageAttachmentFilenameBuilder.buildFileName(metadata);
      gridFsMetadataUpdateService.store(is, filename, "text/plain", metadata);
    }

    mockMvc.perform(delete("/api/data-packages/" + dataPackageId + "/attachments"))
        .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors[0].message",
            containsString("global.error.shadow-delete-not-allowed")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testDeleteAttachmentOfShadowCopyDataPackage() throws Exception {
    String dataPackageId = "ins-issue1991-1.0.0";

    DataPackageAttachmentMetadata metadata =
        UnitTestCreateDomainObjectUtils.buildDataPackageAttachmentMetadata("issue1991");
    metadata.setDataPackageId(dataPackageId);
    metadata.generateId();

    String filename = DataPackageAttachmentFilenameBuilder.buildFileName(metadata);
    try (InputStream is = new ByteArrayInputStream("Test".getBytes(StandardCharsets.UTF_8))) {
      gridFsMetadataUpdateService.store(is, filename, "text/plain", metadata);
    }

    mockMvc
        .perform(delete(
            "/api/data-packages/" + dataPackageId + "/attachments/" + metadata.getFileName()))
        .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors[0].message",
            containsString("global.error.shadow-delete-not-allowed")));
  }

}

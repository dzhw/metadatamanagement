package eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.rest;

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
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.AnalysisPackage;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.AnalysisPackageAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.repository.AnalysisPackageRepository;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.service.AnalysisPackageAttachmentService;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.service.helper.AnalysisPackageAttachmentFilenameBuilder;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.rest.TestUtil;
import eu.dzhw.fdz.metadatamanagement.common.service.GridFsMetadataUpdateService;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

public class AnalysisPackageAttachmentResourceTest extends AbstractTest {
  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private AnalysisPackageRepository analysisPackageRepository;

  @Autowired
  private ElasticsearchUpdateQueueItemRepository elasticsearchUpdateQueueItemRepository;

  @Autowired
  private AnalysisPackageAttachmentService analysisPackageAttachmentService;

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
    this.analysisPackageRepository.deleteAll();
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
    AnalysisPackageAttachmentMetadata analysisPackageAttachmentMetadata =
        UnitTestCreateDomainObjectUtils.buildAnalysisPackageAttachmentMetadata("projectid");
    MockMultipartFile metadata = new MockMultipartFile("analysisPackageAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(analysisPackageAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/analysis-packages/attachments")
        .file(attachment).file(metadata)).andExpect(status().isCreated());

    analysisPackageAttachmentMetadata.generateId();

    // read the created attachment and check the version
    mockMvc
        .perform(get("/api/analysis-packages/"
            + analysisPackageAttachmentMetadata.getAnalysisPackageId() + "/attachments"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[0].analysisPackageId",
            is(analysisPackageAttachmentMetadata.getAnalysisPackageId())))
        .andExpect(jsonPath("$.[0].version", is(0)))
        .andExpect(jsonPath("$.[0].createdBy", is("test")))
        .andExpect(jsonPath("$.[0].lastModifiedBy", is("test")))
        .andExpect(jsonPath("$.[0].masterId",
            is("/public/files/analysis-packages/" + "ana-projectid$/attachments/filename.txt")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER, username = "test")
  public void testDeleteAttachment() throws Exception {
    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    AnalysisPackageAttachmentMetadata analysisPackageAttachmentMetadata =
        UnitTestCreateDomainObjectUtils.buildAnalysisPackageAttachmentMetadata("projectid");
    MockMultipartFile metadata = new MockMultipartFile("analysisPackageAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(analysisPackageAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/analysis-packages/attachments")
        .file(attachment).file(metadata)).andExpect(status().isCreated());

    analysisPackageAttachmentMetadata.generateId();

    // ensure that there is one attachment
    mockMvc
        .perform(get("/api/analysis-packages/"
            + analysisPackageAttachmentMetadata.getAnalysisPackageId() + "/attachments"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(1)));

    // delete the file
    mockMvc
        .perform(delete(
            "/api/analysis-packages/" + analysisPackageAttachmentMetadata.getAnalysisPackageId()
                + "/attachments/" + attachment.getOriginalFilename()))
        .andExpect(status().is2xxSuccessful());

    // ensure the uploaded file does not exist anymore
    mockMvc
        .perform(get("/api/analysis-packages/"
            + analysisPackageAttachmentMetadata.getAnalysisPackageId() + "/attachments"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(0)));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER, username = "test")
  public void testDeleteAllAttachments() throws Exception {
    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    AnalysisPackageAttachmentMetadata analysisPackageAttachmentMetadata =
        UnitTestCreateDomainObjectUtils.buildAnalysisPackageAttachmentMetadata("projectid");
    MockMultipartFile metadata = new MockMultipartFile("analysisPackageAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(analysisPackageAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/analysis-packages/attachments")
        .file(attachment).file(metadata)).andExpect(status().isCreated());

    analysisPackageAttachmentMetadata.generateId();

    // ensure that there is one attachment
    mockMvc
        .perform(get("/api/analysis-packages/"
            + analysisPackageAttachmentMetadata.getAnalysisPackageId() + "/attachments"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(1)));

    // delete the files
    mockMvc
        .perform(delete("/api/analysis-packages/"
            + analysisPackageAttachmentMetadata.getAnalysisPackageId() + "/attachments"))
        .andExpect(status().is2xxSuccessful());

    // ensure the uploaded file does not exist anymore
    mockMvc
        .perform(get("/api/analysis-packages/"
            + analysisPackageAttachmentMetadata.getAnalysisPackageId() + "/attachments"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(0)));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER, username = "test")
  public void testUpdateAttachment() throws Exception {
    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    AnalysisPackageAttachmentMetadata analysisPackageAttachmentMetadata =
        UnitTestCreateDomainObjectUtils.buildAnalysisPackageAttachmentMetadata("projectid");
    MockMultipartFile metadata = new MockMultipartFile("analysisPackageAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(analysisPackageAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/analysis-packages/attachments")
        .file(attachment).file(metadata)).andExpect(status().isCreated());

    analysisPackageAttachmentMetadata.generateId();

    // ensure that there is one attachment
    mockMvc
        .perform(get("/api/analysis-packages/"
            + analysisPackageAttachmentMetadata.getAnalysisPackageId() + "/attachments"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(1)));

    AnalysisPackageAttachmentMetadata current = analysisPackageAttachmentService
        .findAllByAnalysisPackage(analysisPackageAttachmentMetadata.getAnalysisPackageId()).get(0);
    current.setTitle("Updated");

    // update the metadata
    mockMvc.perform(
        put("/api/analysis-packages/" + analysisPackageAttachmentMetadata.getAnalysisPackageId()
            + "/attachments/" + attachment.getOriginalFilename())
                .content(TestUtil.convertObjectToJsonBytes(current))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful());

    // read the created attachment and check the version
    mockMvc
        .perform(get("/api/analysis-packages/"
            + analysisPackageAttachmentMetadata.getAnalysisPackageId() + "/attachments"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[0].analysisPackageId",
            is(analysisPackageAttachmentMetadata.getAnalysisPackageId())))
        .andExpect(jsonPath("$.[0].version", is(1)))
        .andExpect(jsonPath("$.[0].createdBy", is("test")))
        .andExpect(jsonPath("$.[0].lastModifiedBy", is("test")))
        .andExpect(jsonPath("$.[0].masterId",
            is("/public/files/analysis-packages/" + "ana-projectid$/attachments/filename.txt")))
        .andExpect(jsonPath("$.[0].title", is("Updated")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testUploadAttachmentWithMissingDescription() throws Exception {

    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    AnalysisPackageAttachmentMetadata analysisPackageAttachmentMetadata =
        UnitTestCreateDomainObjectUtils.buildAnalysisPackageAttachmentMetadata("projectid");
    analysisPackageAttachmentMetadata.setDescription(new I18nString());

    MockMultipartFile metadata = new MockMultipartFile("analysisPackageAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(analysisPackageAttachmentMetadata));

    mockMvc
        .perform(MockMvcRequestBuilders
            .multipart("/api/analysis-packages/attachments").file(attachment).file(metadata))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].message",
            is("analysis-package-management.error.analysis-package-attachment-metadata"
                + ".description.i18n-string-not-empty")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testAttachmentIsDeletedWithAnalysisPackage() throws Exception {
    AnalysisPackage analysisPackage =
        UnitTestCreateDomainObjectUtils.buildAnalysisPackage("projectid");

    // create the analysis package with the given id
    mockMvc.perform(put("/api/analysis-packages/" + analysisPackage.getId())
        .content(TestUtil.convertObjectToJsonBytes(analysisPackage))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());

    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    AnalysisPackageAttachmentMetadata analysisPackageAttachmentMetadata =
        UnitTestCreateDomainObjectUtils
            .buildAnalysisPackageAttachmentMetadata(analysisPackage.getDataAcquisitionProjectId());

    MockMultipartFile metadata = new MockMultipartFile("analysisPackageAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(analysisPackageAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/analysis-packages/attachments")
        .file(attachment).file(metadata)).andExpect(status().isCreated());

    // delete the analysis package by project id
    mockMvc.perform(delete("/api/analysis-packages/" + analysisPackage.getId()))
        .andExpect(status().isNoContent());

    // check if attachment has been deleted as well
    mockMvc
        .perform(get("/api/analysis-packages/"
            + analysisPackageAttachmentMetadata.getAnalysisPackageId() + "/attachments"))
        .andExpect(status().isOk()).andExpect(content().json("[]"));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testCreateShadowCopyAnalysisPackageAttachment() throws Exception {
    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    AnalysisPackageAttachmentMetadata analysisPackageAttachmentMetadata =
        UnitTestCreateDomainObjectUtils.buildAnalysisPackageAttachmentMetadata("projectid");
    analysisPackageAttachmentMetadata
        .setAnalysisPackageId(analysisPackageAttachmentMetadata.getAnalysisPackageId() + "-1.0.0");
    analysisPackageAttachmentMetadata.generateId();
    MockMultipartFile metadata = new MockMultipartFile("analysisPackageAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(analysisPackageAttachmentMetadata));

    mockMvc
        .perform(MockMvcRequestBuilders.multipart("/api/analysis-packages/attachments")
            .file(attachment).file(metadata))
        .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors[0].message",
            containsString("global.error.shadow-create-not-allowed")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testUpdateAttachmentOfShadowCopyAnalysisPackage() throws Exception {
    String analysisPackageId = "ana-issue1991-1.0.0";
    AnalysisPackageAttachmentMetadata metadata =
        UnitTestCreateDomainObjectUtils.buildAnalysisPackageAttachmentMetadata("issue1991");
    metadata.setAnalysisPackageId(analysisPackageId);
    metadata.generateId();
    metadata.setVersion(0L);

    String filename = AnalysisPackageAttachmentFilenameBuilder.buildFileName(metadata);
    try (InputStream is = new ByteArrayInputStream("Test".getBytes(StandardCharsets.UTF_8))) {
      gridFsMetadataUpdateService.store(is, filename, "text/plain", metadata);
    }

    mockMvc
        .perform(put("/api/analysis-packages/" + analysisPackageId + "/attachments/"
            + metadata.getFileName()).content(TestUtil.convertObjectToJsonBytes(metadata))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors[0].message",
            containsString("global.error.shadow-save-not-allowed")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testDeleteAllAttachmentsOfShadowCopyAnalysisPackage() throws Exception {
    String analysisPackageId = "ana-issue1991-1.0.0";

    AnalysisPackageAttachmentMetadata metadata =
        UnitTestCreateDomainObjectUtils.buildAnalysisPackageAttachmentMetadata("issue1991");
    metadata.setAnalysisPackageId(analysisPackageId);
    metadata.generateId();

    try (InputStream is = new ByteArrayInputStream("Test".getBytes(StandardCharsets.UTF_8))) {
      String filename = AnalysisPackageAttachmentFilenameBuilder.buildFileName(metadata);
      gridFsMetadataUpdateService.store(is, filename, "text/plain", metadata);
    }

    mockMvc.perform(delete("/api/analysis-packages/" + analysisPackageId + "/attachments"))
        .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors[0].message",
            containsString("global.error.shadow-delete-not-allowed")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testDeleteAttachmentOfShadowCopyAnalysisPackage() throws Exception {
    String analysisPackageId = "ana-issue1991-1.0.0";

    AnalysisPackageAttachmentMetadata metadata =
        UnitTestCreateDomainObjectUtils.buildAnalysisPackageAttachmentMetadata("issue1991");
    metadata.setAnalysisPackageId(analysisPackageId);
    metadata.generateId();

    String filename = AnalysisPackageAttachmentFilenameBuilder.buildFileName(metadata);
    try (InputStream is = new ByteArrayInputStream("Test".getBytes(StandardCharsets.UTF_8))) {
      gridFsMetadataUpdateService.store(is, filename, "text/plain", metadata);
    }

    mockMvc
        .perform(delete("/api/analysis-packages/" + analysisPackageId + "/attachments/"
            + metadata.getFileName()))
        .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors[0].message",
            containsString("global.error.shadow-delete-not-allowed")));
  } 
}

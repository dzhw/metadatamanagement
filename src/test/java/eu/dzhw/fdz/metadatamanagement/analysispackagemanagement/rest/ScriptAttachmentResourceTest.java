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

import org.javers.common.collections.Lists;
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
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.Script;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.ScriptAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.repository.AnalysisPackageRepository;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.service.ScriptAttachmentService;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.service.helper.ScriptAttachmentFilenameBuilder;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.rest.TestUtil;
import eu.dzhw.fdz.metadatamanagement.common.service.GridFsMetadataUpdateService;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

public class ScriptAttachmentResourceTest extends AbstractTest {
  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private AnalysisPackageRepository analysisPackageRepository;

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
  
  @Autowired
  private ScriptAttachmentService scriptAttachmentService;

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
    AnalysisPackage analysisPackage =
        UnitTestCreateDomainObjectUtils.buildAnalysisPackage("projectid");
    analysisPackageRepository.save(analysisPackage);
    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    ScriptAttachmentMetadata scriptAttachmentMetadata =
        UnitTestCreateDomainObjectUtils.buildScriptAttachmentMetadata(analysisPackage);
    MockMultipartFile metadata = new MockMultipartFile("scriptAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(scriptAttachmentMetadata));

    mockMvc
        .perform(
            MockMvcRequestBuilders
                .multipart("/api/analysis-packages/"
                    + scriptAttachmentMetadata.getAnalysisPackageId() + "/scripts/attachments")
                .file(attachment).file(metadata))
        .andExpect(status().isCreated());

    scriptAttachmentMetadata.generateId();

    // read the created attachment and check the version
    mockMvc
        .perform(get("/api/analysis-packages/"
            + scriptAttachmentMetadata.getAnalysisPackageId() + "/scripts/attachments"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[0].analysisPackageId",
            is(scriptAttachmentMetadata.getAnalysisPackageId())))
        .andExpect(jsonPath("$.[0].version", is(0)))
        .andExpect(jsonPath("$.[0].createdBy", is("test")))
        .andExpect(jsonPath("$.[0].lastModifiedBy", is("test")))
        .andExpect(jsonPath("$.[0].masterId",
            is("/public/files/analysis-packages/" + "ana-projectid$/scripts/"
                + scriptAttachmentMetadata.getScriptUuid() + "/attachments/filename.txt")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER, username = "test")
  public void shouldDeleteAttachmentIfScriptIsDeleted() throws Exception {
    AnalysisPackage analysisPackage =
        UnitTestCreateDomainObjectUtils.buildAnalysisPackage("projectid");
    analysisPackage = analysisPackageRepository.save(analysisPackage);
    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    ScriptAttachmentMetadata scriptAttachmentMetadata =
        UnitTestCreateDomainObjectUtils.buildScriptAttachmentMetadata(analysisPackage);
    MockMultipartFile metadata = new MockMultipartFile("scriptAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(scriptAttachmentMetadata));

    mockMvc
        .perform(
            MockMvcRequestBuilders
                .multipart("/api/analysis-packages/"
                    + scriptAttachmentMetadata.getAnalysisPackageId() + "/scripts/attachments")
                .file(attachment).file(metadata))
        .andExpect(status().isCreated());

    scriptAttachmentMetadata.generateId();

    // read the created attachment
    mockMvc
        .perform(get("/api/analysis-packages/"
            + scriptAttachmentMetadata.getAnalysisPackageId() + "/scripts/attachments"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[0].analysisPackageId",
            is(scriptAttachmentMetadata.getAnalysisPackageId())))
        .andExpect(jsonPath("$.[0].masterId",
            is("/public/files/analysis-packages/" + "ana-projectid$/scripts/"
                + scriptAttachmentMetadata.getScriptUuid() + "/attachments/filename.txt")));

    analysisPackage.setScripts(
        Lists.asList(Script.builder().softwarePackage("R").softwarePackageVersion("1.0.0")
            .title(new I18nString("de", "en")).uuid("5432").usedLanguage("de").build()));
    // update the analysis package
    mockMvc.perform(put("/api/analysis-packages/" + "/" + analysisPackage.getId())
        .content(TestUtil.convertObjectToJsonBytes(analysisPackage))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().is2xxSuccessful());

    // check that the script attachment has been deleted
    mockMvc
        .perform(get("/api/analysis-packages/" + scriptAttachmentMetadata.getAnalysisPackageId()
            + "/scripts/attachments"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(0)));
  }
  
  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER, username = "test")
  public void shouldNotDeleteAttachmentIfScriptIsModified() throws Exception {
    AnalysisPackage analysisPackage =
        UnitTestCreateDomainObjectUtils.buildAnalysisPackage("projectid");
    analysisPackage = analysisPackageRepository.save(analysisPackage);
    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    ScriptAttachmentMetadata scriptAttachmentMetadata =
        UnitTestCreateDomainObjectUtils.buildScriptAttachmentMetadata(analysisPackage);
    MockMultipartFile metadata = new MockMultipartFile("scriptAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(scriptAttachmentMetadata));

    mockMvc
        .perform(
            MockMvcRequestBuilders
                .multipart("/api/analysis-packages/"
                    + scriptAttachmentMetadata.getAnalysisPackageId() + "/scripts/attachments")
                .file(attachment).file(metadata))
        .andExpect(status().isCreated());

    scriptAttachmentMetadata.generateId();

    // read the created attachment
    mockMvc
        .perform(get("/api/analysis-packages/"
            + scriptAttachmentMetadata.getAnalysisPackageId() + "/scripts/attachments"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[0].analysisPackageId",
            is(scriptAttachmentMetadata.getAnalysisPackageId())))
        .andExpect(jsonPath("$.[0].masterId",
            is("/public/files/analysis-packages/" + "ana-projectid$/scripts/"
                + scriptAttachmentMetadata.getScriptUuid() + "/attachments/filename.txt")));

    analysisPackage.getScripts().get(0).setTitle(new I18nString("Neuer Titel", "New Title"));
    // update the analysis package
    mockMvc.perform(put("/api/analysis-packages/" + "/" + analysisPackage.getId())
        .content(TestUtil.convertObjectToJsonBytes(analysisPackage))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().is2xxSuccessful());

    // check that the script attachment has NOT been deleted
    mockMvc
        .perform(get("/api/analysis-packages/" + scriptAttachmentMetadata.getAnalysisPackageId()
            + "/scripts/attachments"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(1)));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER, username = "test")
  public void testDeleteAttachment() throws Exception {
    AnalysisPackage analysisPackage =
        UnitTestCreateDomainObjectUtils.buildAnalysisPackage("projectid");
    analysisPackageRepository.save(analysisPackage);

    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    ScriptAttachmentMetadata scriptAttachmentMetadata =
        UnitTestCreateDomainObjectUtils.buildScriptAttachmentMetadata(analysisPackage);
    MockMultipartFile metadata = new MockMultipartFile("scriptAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(scriptAttachmentMetadata));

    mockMvc
        .perform(
            MockMvcRequestBuilders
                .multipart("/api/analysis-packages/"
                    + scriptAttachmentMetadata.getAnalysisPackageId() + "/scripts/attachments")
                .file(attachment).file(metadata))
        .andExpect(status().isCreated());

    scriptAttachmentMetadata.generateId();

    // ensure that there is one attachment
    mockMvc
        .perform(get("/api/analysis-packages/" + scriptAttachmentMetadata.getAnalysisPackageId()
            + "/scripts/attachments"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(1)));

    // delete the file
    mockMvc
        .perform(delete("/api/analysis-packages/" + scriptAttachmentMetadata.getAnalysisPackageId()
            + "/scripts/" + scriptAttachmentMetadata.getScriptUuid() + "/attachments/"
            + attachment.getOriginalFilename()))
        .andExpect(status().is2xxSuccessful());

    // ensure the uploaded file does not exist anymore
    mockMvc
        .perform(get("/api/analysis-packages/" + scriptAttachmentMetadata.getAnalysisPackageId()
            + "/scripts/attachments"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(0)));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER, username = "test")
  public void testDeleteAllAttachments() throws Exception {
    AnalysisPackage analysisPackage =
        UnitTestCreateDomainObjectUtils.buildAnalysisPackage("projectid");
    analysisPackageRepository.save(analysisPackage);
    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    ScriptAttachmentMetadata scriptAttachmentMetadata =
        UnitTestCreateDomainObjectUtils.buildScriptAttachmentMetadata(analysisPackage);
    MockMultipartFile metadata = new MockMultipartFile("scriptAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(scriptAttachmentMetadata));

    mockMvc
        .perform(
            MockMvcRequestBuilders
                .multipart("/api/analysis-packages/"
                    + scriptAttachmentMetadata.getAnalysisPackageId() + "/scripts/attachments")
                .file(attachment).file(metadata))
        .andExpect(status().isCreated());

    scriptAttachmentMetadata.generateId();

    // ensure that there is one attachment
    mockMvc
        .perform(get("/api/analysis-packages/" + scriptAttachmentMetadata.getAnalysisPackageId()
            + "/scripts/attachments"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(1)));

    // delete the files
    mockMvc.perform(delete("/api/analysis-packages/"
        + scriptAttachmentMetadata.getAnalysisPackageId() + "/scripts/attachments"))
        .andExpect(status().is2xxSuccessful());

    // ensure the uploaded file does not exist anymore
    mockMvc
        .perform(get("/api/analysis-packages/" + scriptAttachmentMetadata.getAnalysisPackageId()
            + "/scripts/attachments"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(0)));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testUploadAttachmentWithMissingUuid() throws Exception {
    AnalysisPackage analysisPackage =
        UnitTestCreateDomainObjectUtils.buildAnalysisPackage("projectid");
    analysisPackageRepository.save(analysisPackage);
    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    ScriptAttachmentMetadata scriptAttachmentMetadata =
        UnitTestCreateDomainObjectUtils.buildScriptAttachmentMetadata(analysisPackage);
    scriptAttachmentMetadata.setScriptUuid(null);

    MockMultipartFile metadata = new MockMultipartFile("scriptAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(scriptAttachmentMetadata));

    mockMvc
        .perform(
            MockMvcRequestBuilders
                .multipart("/api/analysis-packages/"
                    + scriptAttachmentMetadata.getAnalysisPackageId() + "/scripts/attachments")
                .file(attachment).file(metadata))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].message",
            is("analysis-package-management.error.script-attachment-metadata"
                + ".script-uuid.not-empty")));
  }
  
  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void shouldRemoveOrphanedAttachment() throws Exception {
    AnalysisPackage analysisPackage =
        UnitTestCreateDomainObjectUtils.buildAnalysisPackage("projectid");
    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    ScriptAttachmentMetadata scriptAttachmentMetadata =
        UnitTestCreateDomainObjectUtils.buildScriptAttachmentMetadata(analysisPackage);
    MockMultipartFile metadata = new MockMultipartFile("scriptAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(scriptAttachmentMetadata));
    
    analysisPackageRepository.save(analysisPackage);
    
    //upload the attachment
    mockMvc
    .perform(
        MockMvcRequestBuilders
            .multipart("/api/analysis-packages/"
                + scriptAttachmentMetadata.getAnalysisPackageId() + "/scripts/attachments")
            .file(attachment).file(metadata))
    .andExpect(status().isCreated());
    
    //change script uuid to make attachment orphaned
    analysisPackage.getScripts().get(0).setUuid("etataetataetaettaeet");
    analysisPackageRepository.save(analysisPackage);
    
    // check that the script attachment is available
    mockMvc
        .perform(get("/api/analysis-packages/" + scriptAttachmentMetadata.getAnalysisPackageId()
            + "/scripts/attachments"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(1)));
    
    scriptAttachmentService.removeOrphanedAttachments();
    
    // check that the script attachment has been deleted
    mockMvc
        .perform(get("/api/analysis-packages/" + scriptAttachmentMetadata.getAnalysisPackageId()
            + "/scripts/attachments"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(0)));
  }
  
  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void shouldNotRemoveAttachment() throws Exception {
    AnalysisPackage analysisPackage =
        UnitTestCreateDomainObjectUtils.buildAnalysisPackage("projectid");
    analysisPackageRepository.save(analysisPackage);
    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    ScriptAttachmentMetadata scriptAttachmentMetadata =
        UnitTestCreateDomainObjectUtils.buildScriptAttachmentMetadata(analysisPackage);
    MockMultipartFile metadata = new MockMultipartFile("scriptAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(scriptAttachmentMetadata));
    
    //upload the attachment
    mockMvc
    .perform(
        MockMvcRequestBuilders
            .multipart("/api/analysis-packages/"
                + scriptAttachmentMetadata.getAnalysisPackageId() + "/scripts/attachments")
            .file(attachment).file(metadata))
    .andExpect(status().isCreated());
    
    // check that the script attachment is available
    mockMvc
        .perform(get("/api/analysis-packages/" + scriptAttachmentMetadata.getAnalysisPackageId()
            + "/scripts/attachments"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(1)));
    
    scriptAttachmentService.removeOrphanedAttachments();
    
    // check that the script attachment is still available
    mockMvc
        .perform(get("/api/analysis-packages/" + scriptAttachmentMetadata.getAnalysisPackageId()
            + "/scripts/attachments"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(1)));
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
    ScriptAttachmentMetadata scriptAttachmentMetadata =
        UnitTestCreateDomainObjectUtils.buildScriptAttachmentMetadata(analysisPackage);

    MockMultipartFile metadata = new MockMultipartFile("scriptAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(scriptAttachmentMetadata));

    mockMvc
        .perform(
            MockMvcRequestBuilders
                .multipart("/api/analysis-packages/"
                    + scriptAttachmentMetadata.getAnalysisPackageId() + "/scripts/attachments")
                .file(attachment).file(metadata))
        .andExpect(status().isCreated());

    // delete the analysis package by id
    mockMvc.perform(delete("/api/analysis-packages/" + analysisPackage.getId()))
        .andExpect(status().isNoContent());

    // check if attachment has been deleted as well
    mockMvc.perform(get("/api/analysis-packages/" + scriptAttachmentMetadata.getAnalysisPackageId()
        + "/scripts/attachments")).andExpect(status().isOk()).andExpect(content().json("[]"));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testCreateShadowCopyScriptAttachment() throws Exception {
    AnalysisPackage analysisPackage =
        UnitTestCreateDomainObjectUtils.buildAnalysisPackage("projectid");
    analysisPackage.setId(analysisPackage.getId() + "-1.0.0");
    analysisPackageRepository.save(analysisPackage);
    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    ScriptAttachmentMetadata scriptAttachmentMetadata =
        UnitTestCreateDomainObjectUtils.buildScriptAttachmentMetadata(analysisPackage);
    scriptAttachmentMetadata.generateId();
    MockMultipartFile metadata = new MockMultipartFile("scriptAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(scriptAttachmentMetadata));

    mockMvc
        .perform(
            MockMvcRequestBuilders
                .multipart("/api/analysis-packages/"
                    + scriptAttachmentMetadata.getAnalysisPackageId() + "/scripts/attachments")
                .file(attachment).file(metadata))
        .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors[0].message",
            containsString("global.error.shadow-create-not-allowed")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testDeleteAllScriptAttachmentsOfShadowCopyAnalysisPackage() throws Exception {
    AnalysisPackage analysisPackage =
        UnitTestCreateDomainObjectUtils.buildAnalysisPackage("projectid");
    analysisPackage.setId(analysisPackage.getId() + "-1.0.0");
    analysisPackageRepository.save(analysisPackage);

    ScriptAttachmentMetadata metadata =
        UnitTestCreateDomainObjectUtils.buildScriptAttachmentMetadata(analysisPackage);
    metadata.setAnalysisPackageId(analysisPackage.getId());
    metadata.generateId();

    try (InputStream is = new ByteArrayInputStream("Test".getBytes(StandardCharsets.UTF_8))) {
      String filename = ScriptAttachmentFilenameBuilder.buildFileName(metadata);
      gridFsMetadataUpdateService.store(is, filename, "text/plain", metadata);
    }

    mockMvc
        .perform(
            delete("/api/analysis-packages/" + analysisPackage.getId() + "/scripts/attachments"))
        .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors[0].message",
            containsString("global.error.shadow-delete-not-allowed")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testDeleteAttachmentOfShadowCopyAnalysisPackage() throws Exception {
    AnalysisPackage analysisPackage =
        UnitTestCreateDomainObjectUtils.buildAnalysisPackage("projectid");
    analysisPackage.setId(analysisPackage.getId() + "-1.0.0");
    analysisPackageRepository.save(analysisPackage);

    ScriptAttachmentMetadata metadata =
        UnitTestCreateDomainObjectUtils.buildScriptAttachmentMetadata(analysisPackage);
    metadata.setAnalysisPackageId(analysisPackage.getId());
    metadata.generateId();

    String filename = ScriptAttachmentFilenameBuilder.buildFileName(metadata);
    try (InputStream is = new ByteArrayInputStream("Test".getBytes(StandardCharsets.UTF_8))) {
      gridFsMetadataUpdateService.store(is, filename, "text/plain", metadata);
    }

    mockMvc
        .perform(delete("/api/analysis-packages/" + analysisPackage.getId() + "/scripts/"
            + metadata.getScriptUuid() + "/attachments/" + metadata.getFileName()))
        .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors[0].message",
            containsString("global.error.shadow-delete-not-allowed")));
  }

}

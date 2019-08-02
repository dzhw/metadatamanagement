package eu.dzhw.fdz.metadatamanagement.studymanagement.rest;

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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.mongodb.gridfs.GridFS;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.rest.TestUtil;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.StudyAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.studymanagement.repository.StudyRepository;
import eu.dzhw.fdz.metadatamanagement.studymanagement.service.helper.StudyAttachmentFilenameBuilder;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

public class StudyAttachmentResourceTest extends AbstractTest {
  @Autowired
  private WebApplicationContext wac;
  
  @Autowired
  private StudyRepository studyRepository;
  
  @Autowired
  private ElasticsearchUpdateQueueItemRepository elasticsearchUpdateQueueItemRepository;
  
  @Autowired
  private ElasticsearchAdminService elasticsearchAdminService;
  
  @Autowired
  private JaversService javersService;

  @Autowired
  private GridFsOperations gridFsOperations;

  @Autowired
  private GridFS gridFs;

  private MockMvc mockMvc;

  @Before
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
      .build();
  }

  @After
  public void cleanUp() {
    this.studyRepository.deleteAll();
    this.elasticsearchUpdateQueueItemRepository.deleteAll();
    this.elasticsearchAdminService.recreateAllIndices();
    this.javersService.deleteAll();
    this.gridFs.getFileList().iterator().forEachRemaining(gridFs::remove);
  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER, username="test")
  public void testUploadValidAttachment() throws Exception {

    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    StudyAttachmentMetadata studyAttachmentMetadata = UnitTestCreateDomainObjectUtils
      .buildStudyAttachmentMetadata("projectid");
    MockMultipartFile metadata = new MockMultipartFile("studyAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(studyAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/studies/attachments")
      .file(attachment)
      .file(metadata))
      .andExpect(status().isCreated());

    studyAttachmentMetadata.generateId();

    // read the created attachment and check the version
    mockMvc.perform(
        get("/api/studies/" + studyAttachmentMetadata.getStudyId() + "/attachments"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.[0].studyId", is(studyAttachmentMetadata.getStudyId())))
      .andExpect(jsonPath("$.[0].version", is(0)))
      .andExpect(jsonPath("$.[0].createdBy", is("test")))
      .andExpect(jsonPath("$.[0].lastModifiedBy", is("test")))
      .andExpect(jsonPath("$.[0].masterId", is("/public/files/studies/"
          + "stu-projectid$/attachments/filename.txt")));
  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testUploadAttachmentWithWrongType() throws Exception {

    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    StudyAttachmentMetadata studyAttachmentMetadata = UnitTestCreateDomainObjectUtils
      .buildStudyAttachmentMetadata("projectid");
    studyAttachmentMetadata.setType(new I18nString("hurz","hurz"));
    MockMultipartFile metadata = new MockMultipartFile("studyAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(studyAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/studies/attachments")
      .file(attachment)
      .file(metadata))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.errors[0].message",
          is("study-management.error.study-attachment-metadata.type.valid-type")));
  }
  
  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testUploadAttachmentWithMissingDescription() throws Exception {

    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    StudyAttachmentMetadata studyAttachmentMetadata = UnitTestCreateDomainObjectUtils
      .buildStudyAttachmentMetadata("projectid");
    studyAttachmentMetadata.setDescription(new I18nString());

    MockMultipartFile metadata = new MockMultipartFile("studyAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(studyAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/studies/attachments")
      .file(attachment)
      .file(metadata))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.errors[0].message",
          is("study-management.error.study-attachment-metadata.description.i18n-string-not-empty")));
  }
  
  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testAttachmentIsDeletedWithStudy() throws Exception {
    Study study =
        UnitTestCreateDomainObjectUtils.buildStudy("projectid");

    // create the study with the given id
    mockMvc.perform(put("/api/studies/" + study.getId())
      .content(TestUtil.convertObjectToJsonBytes(study)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated());
    
    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    StudyAttachmentMetadata studyAttachmentMetadata = UnitTestCreateDomainObjectUtils
      .buildStudyAttachmentMetadata(study.getDataAcquisitionProjectId());

    MockMultipartFile metadata = new MockMultipartFile("studyAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(studyAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/studies/attachments")
      .file(attachment)
      .file(metadata))
      .andExpect(status().isCreated());
    
    // delete the study by project id
    mockMvc.perform(delete("/api/studies/" + study.getId()))
      .andExpect(status().isNoContent());
    
    // check if attachment has been deleted as well
    mockMvc.perform(
        get("/api/studies/" + studyAttachmentMetadata.getStudyId() + "/attachments"))
      .andExpect(status().isOk())
      .andExpect(content().json("[]"));
  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testCreateShadowCopyInstrumentAttachment() throws Exception {
    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    StudyAttachmentMetadata studyAttachmentMetadata = UnitTestCreateDomainObjectUtils
        .buildStudyAttachmentMetadata("projectid");
    studyAttachmentMetadata.setStudyId(studyAttachmentMetadata.getStudyId() + "-1.0.0");
    studyAttachmentMetadata.generateId();
    MockMultipartFile metadata = new MockMultipartFile("studyAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(studyAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/studies/attachments")
        .file(attachment)
        .file(metadata))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].message", containsString("global.error.shadow-create-not-allowed")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testUpdateAttachmentOfShadowCopyStudy() throws Exception {
    String studyId = "stu-issue1991-1.0.0";
    StudyAttachmentMetadata metadata = UnitTestCreateDomainObjectUtils
        .buildStudyAttachmentMetadata("issue1991");
    metadata.setStudyId(studyId);
    metadata.generateId();
    metadata.setVersion(0L);

    String filename = StudyAttachmentFilenameBuilder.buildFileName(metadata);
    try (InputStream is = new ByteArrayInputStream("Test".getBytes(StandardCharsets.UTF_8))) {
      gridFsOperations.store(is, filename, "text/plain", metadata);
    }

    mockMvc.perform(put("/api/studies/" + studyId + "/attachments/" + metadata.getFileName())
        .content(TestUtil.convertObjectToJsonBytes(metadata))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].message", containsString("global.error.shadow-save-not-allowed")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testDeleteAllAttachmentsOfShadowCopyStudy() throws Exception {
    String studyId = "stu-issue1991-1.0.0";

    StudyAttachmentMetadata metadata = UnitTestCreateDomainObjectUtils
        .buildStudyAttachmentMetadata("issue1991");
    metadata.setStudyId(studyId);
    metadata.generateId();

    try (InputStream is = new ByteArrayInputStream("Test".getBytes(StandardCharsets.UTF_8))) {
      String filename = StudyAttachmentFilenameBuilder.buildFileName(metadata);
      gridFsOperations.store(is, filename, "text/plain", metadata);
    }

    mockMvc.perform(delete("/api/studies/" + studyId + "/attachments"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].message", containsString("global.error.shadow-delete-not-allowed")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testDeleteAttachmentOfShadowCopyStudy() throws Exception {
    String studyId = "ins-issue1991-1.0.0";

    StudyAttachmentMetadata metadata = UnitTestCreateDomainObjectUtils
        .buildStudyAttachmentMetadata("issue1991");
    metadata.setStudyId(studyId);
    metadata.generateId();

    String filename = StudyAttachmentFilenameBuilder.buildFileName(metadata);
    try (InputStream is = new ByteArrayInputStream("Test".getBytes(StandardCharsets.UTF_8))) {
      gridFsOperations.store(is, filename, "text/plain", metadata);
    }

    mockMvc.perform(delete("/api/studies/" + studyId + "/attachments/" + metadata.getFileName()))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].message", containsString("global.error.shadow-delete-not-allowed")));
  }

}

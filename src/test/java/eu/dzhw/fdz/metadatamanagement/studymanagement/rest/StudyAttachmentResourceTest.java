package eu.dzhw.fdz.metadatamanagement.studymanagement.rest;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.rest.TestUtil;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.StudyAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.studymanagement.repository.StudyRepository;
import eu.dzhw.fdz.metadatamanagement.studymanagement.service.StudyAttachmentService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

public class StudyAttachmentResourceTest extends AbstractTest {
  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private StudyAttachmentService studyAttachmentService;
  
  @Autowired
  private StudyRepository studyRepository;

  private MockMvc mockMvc;

  @Before
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
      .build();
  }

  @After
  public void cleanUp() {
    this.studyRepository.deleteAll();
    this.studyAttachmentService.deleteAll();
  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER, username="test")
  public void testUploadValidAttachment() throws Exception {

    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    StudyAttachmentMetadata studyAttachmentMetadata = UnitTestCreateDomainObjectUtils
      .buildStudyAttachmentMetadata("projectId");
    MockMultipartFile metadata = new MockMultipartFile("studyAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(studyAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.fileUpload("/api/studies/attachments")
      .file(attachment)
      .file(metadata))
      .andExpect(status().isCreated());

    // read the created attachment and check the version
    mockMvc.perform(
        get("/api/studies/" + studyAttachmentMetadata.getStudyId() + "/attachments"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.[0].studyId", is(studyAttachmentMetadata.getStudyId())))
      .andExpect(jsonPath("$.[0].version", is(0)))
      .andExpect(jsonPath("$.[0].createdBy", is("test")))
      .andExpect(jsonPath("$.[0].lastModifiedBy", is("test")));
  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testUploadAttachmentWithWrongType() throws Exception {

    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    StudyAttachmentMetadata studyAttachmentMetadata = UnitTestCreateDomainObjectUtils
      .buildStudyAttachmentMetadata("projectId");
    studyAttachmentMetadata.getType().setDe("hurz");
    MockMultipartFile metadata = new MockMultipartFile("studyAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(studyAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.fileUpload("/api/studies/attachments")
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
      .buildStudyAttachmentMetadata("projectId");
    studyAttachmentMetadata.setDescription(new I18nString());

    MockMultipartFile metadata = new MockMultipartFile("studyAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(studyAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.fileUpload("/api/studies/attachments")
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
        UnitTestCreateDomainObjectUtils.buildStudy("projectId");

    // create the study with the given id
    mockMvc.perform(put("/api/studies/" + study.getId())
      .content(TestUtil.convertObjectToJsonBytes(study)))
      .andExpect(status().isCreated());
    
    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    StudyAttachmentMetadata studyAttachmentMetadata = UnitTestCreateDomainObjectUtils
      .buildStudyAttachmentMetadata(study.getDataAcquisitionProjectId());

    MockMultipartFile metadata = new MockMultipartFile("studyAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(studyAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.fileUpload("/api/studies/attachments")
      .file(attachment)
      .file(metadata))
      .andExpect(status().isCreated());
    
    // delete the study by project id
    mockMvc.perform(post("/api/studies/delete").param("dataAcquisitionProjectId", study.getDataAcquisitionProjectId())
      .content(TestUtil.convertObjectToJsonBytes(study)))
      .andExpect(status().isOk());
    
    // check if attachment has been deleted as well
    mockMvc.perform(
        get("/api/studies/" + studyAttachmentMetadata.getStudyId() + "/attachments"))
      .andExpect(status().isOk())
      .andExpect(content().json("[]"));
  }

}

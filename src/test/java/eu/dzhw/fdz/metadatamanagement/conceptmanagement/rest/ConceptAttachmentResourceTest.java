package eu.dzhw.fdz.metadatamanagement.conceptmanagement.rest;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.Concept;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.ConceptAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.repository.ConceptRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

public class ConceptAttachmentResourceTest extends AbstractTest {
  @Autowired
  private WebApplicationContext wac;
  
  @Autowired
  private ConceptRepository conceptRepository;
  
  @Autowired
  private ElasticsearchUpdateQueueItemRepository elasticsearchUpdateQueueItemRepository;
  
  @Autowired
  private JaversService javersService;

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
    this.conceptRepository.deleteAll();
    this.elasticsearchUpdateQueueItemRepository.deleteAll();
    this.javersService.deleteAll();
    this.gridFs.getFileList().iterator().forEachRemaining(gridFs::remove);
  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER, username="test")
  public void testUploadValidAttachment() throws Exception {

    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    ConceptAttachmentMetadata conceptAttachmentMetadata = UnitTestCreateDomainObjectUtils
      .buildConceptAttachmentMetadata("con-conceptid$");
    MockMultipartFile metadata = new MockMultipartFile("conceptAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(conceptAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/concepts/attachments")
      .file(attachment)
      .file(metadata))
      .andExpect(status().isCreated());

    conceptAttachmentMetadata.generateId();

    // read the created attachment and check the version
    mockMvc.perform(
        get("/api/concepts/" + conceptAttachmentMetadata.getConceptId() + "/attachments"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.[0].conceptId", is(conceptAttachmentMetadata.getConceptId())))
      .andExpect(jsonPath("$.[0].version", is(0)))
      .andExpect(jsonPath("$.[0].createdBy", is("test")))
      .andExpect(jsonPath("$.[0].lastModifiedBy", is("test")))
      .andExpect(jsonPath("$.[0].id", is("/public/files/concepts/"
          + "con-conceptid$/attachments/filename.txt")));
  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testUploadAttachmentWithWrongType() throws Exception {

    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    ConceptAttachmentMetadata conceptAttachmentMetadata = UnitTestCreateDomainObjectUtils
      .buildConceptAttachmentMetadata("con-conceptid$");
    conceptAttachmentMetadata.setType(new I18nString("hurz","hurz"));
    MockMultipartFile metadata = new MockMultipartFile("conceptAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(conceptAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/concepts/attachments")
      .file(attachment)
      .file(metadata))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.errors[0].message",
          is("concept-management.error.concept-attachment-metadata.type.valid-type")));
  }
  
  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testUploadAttachmentWithMissingDescription() throws Exception {

    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    ConceptAttachmentMetadata conceptAttachmentMetadata = UnitTestCreateDomainObjectUtils
      .buildConceptAttachmentMetadata("con-conceptid$");
    conceptAttachmentMetadata.setDescription(new I18nString());

    MockMultipartFile metadata = new MockMultipartFile("conceptAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(conceptAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/concepts/attachments")
      .file(attachment)
      .file(metadata))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.errors[0].message",
          is("concept-management.error.concept-attachment-metadata.description.i18n-string-not-empty")));
  }
  
  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testAttachmentIsDeletedWithConcept() throws Exception {
    Concept concept =
        UnitTestCreateDomainObjectUtils.buildConcept();

    // create the concept with the given id
    mockMvc.perform(put("/api/concepts/" + concept.getId())
      .content(TestUtil.convertObjectToJsonBytes(concept)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated());
    
    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    ConceptAttachmentMetadata conceptAttachmentMetadata = UnitTestCreateDomainObjectUtils
      .buildConceptAttachmentMetadata(concept.getId());

    MockMultipartFile metadata = new MockMultipartFile("conceptAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(conceptAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/concepts/attachments")
      .file(attachment)
      .file(metadata))
      .andExpect(status().isCreated());
    
    // delete the concept by id
    mockMvc.perform(delete("/api/concepts/" + concept.getId()))
      .andExpect(status().isNoContent());
    
    // check if attachment has been deleted as well
    mockMvc.perform(
        get("/api/concepts/" + conceptAttachmentMetadata.getConceptId() + "/attachments"))
      .andExpect(status().isOk())
      .andExpect(content().json("[]"));
  }
}

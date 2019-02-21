package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.rest;

import com.mongodb.gridfs.GridFS;
import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.rest.TestUtil;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.InstrumentAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository.InstrumentRepository;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.service.InstrumentAttachmentFilenameBuilder;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class InstrumentAttachmentResourceTest extends AbstractTest {
  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private InstrumentRepository instrumentRepository;
  
  @Autowired
  private ElasticsearchUpdateQueueItemRepository elasticsearchUpdateQueueItemRepository;
  
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
    this.instrumentRepository.deleteAll();
    this.elasticsearchUpdateQueueItemRepository.deleteAll();
    this.javersService.deleteAll();
    this.gridFs.getFileList().iterator().forEachRemaining(gridFs::remove);
  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER, username="test")
  public void testUploadValidAttachment() throws Exception {

    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    InstrumentAttachmentMetadata instrumentAttachmentMetadata = UnitTestCreateDomainObjectUtils
      .buildInstrumentAttachmentMetadata("projectid", 1);
    MockMultipartFile metadata = new MockMultipartFile("instrumentAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(instrumentAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/instruments/attachments")
      .file(attachment)
      .file(metadata))
      .andExpect(status().isCreated());

    instrumentAttachmentMetadata.generateId();

    // read the created attachment and check the version
    mockMvc.perform(
        get("/api/instruments/" + instrumentAttachmentMetadata.getInstrumentId() + "/attachments"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.[0].instrumentId", is(instrumentAttachmentMetadata.getInstrumentId())))
      .andExpect(jsonPath("$.[0].version", is(0)))
      .andExpect(jsonPath("$.[0].createdBy", is("test")))
      .andExpect(jsonPath("$.[0].lastModifiedBy", is("test")))
      .andExpect(jsonPath("$.[0].masterId", is(instrumentAttachmentMetadata.getId())));
  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testUploadAttachmentWithWrongType() throws Exception {

    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    InstrumentAttachmentMetadata instrumentAttachmentMetadata = UnitTestCreateDomainObjectUtils
      .buildInstrumentAttachmentMetadata("projectid", 1);
    instrumentAttachmentMetadata.setType(new I18nString("hurz", "hurz"));
    MockMultipartFile metadata = new MockMultipartFile("instrumentAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(instrumentAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/instruments/attachments")
      .file(attachment)
      .file(metadata))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.errors[0].message",
          is("instrument-management.error.instrument-attachment-metadata.type.valid-type")));
  }
  
  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testUploadAttachmentWithMissingDescription() throws Exception {

    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    InstrumentAttachmentMetadata instrumentAttachmentMetadata = UnitTestCreateDomainObjectUtils
      .buildInstrumentAttachmentMetadata("projectid", 1);
    instrumentAttachmentMetadata.setDescription(new I18nString());

    MockMultipartFile metadata = new MockMultipartFile("instrumentAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(instrumentAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/instruments/attachments")
      .file(attachment)
      .file(metadata))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.errors[0].message",
          is("instrument-management.error.instrument-attachment-metadata.description.i18n-string-not-empty")));
  }
  
  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testAttachmentIsDeletedWithInstrument() throws Exception {
    Instrument instrument =
        UnitTestCreateDomainObjectUtils.buildInstrument("projectid", "projectid-sy1");

    // create the instrument with the given id
    mockMvc.perform(put("/api/instruments/" + instrument.getId())
      .content(TestUtil.convertObjectToJsonBytes(instrument)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated());
    
    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    InstrumentAttachmentMetadata instrumentAttachmentMetadata = UnitTestCreateDomainObjectUtils
      .buildInstrumentAttachmentMetadata(instrument.getDataAcquisitionProjectId(), instrument.getNumber());

    MockMultipartFile metadata = new MockMultipartFile("instrumentAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(instrumentAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/instruments/attachments")
      .file(attachment)
      .file(metadata))
      .andExpect(status().isCreated());
    
    // delete the instrument by project id
    mockMvc.perform(delete("/api/instruments/" + instrument.getId()))
      .andExpect(status().isNoContent());
    
    // check if attachment has been deleted as well
    mockMvc.perform(
        get("/api/instruments/" + instrumentAttachmentMetadata.getInstrumentId() + "/attachments"))
      .andExpect(status().isOk())
      .andExpect(content().json("[]"));
  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testCreateShadowCopyInstrumentAttachment() throws Exception {
    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    InstrumentAttachmentMetadata instrumentAttachmentMetadata = UnitTestCreateDomainObjectUtils
        .buildInstrumentAttachmentMetadata("projectid", 1);
    instrumentAttachmentMetadata.setInstrumentId(instrumentAttachmentMetadata.getInstrumentId() + "-1.0.0");
    instrumentAttachmentMetadata.generateId();
    MockMultipartFile metadata = new MockMultipartFile("instrumentAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(instrumentAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/instruments/attachments")
        .file(attachment)
        .file(metadata))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].message", containsString("global.error.shadow-create-not-allowed")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testUpdateAttachmentOfShadowCopyInstrument() throws Exception {
    String instrumentId = "ins-issue1991-ins1$-1.0.0";
    InstrumentAttachmentMetadata metadata = UnitTestCreateDomainObjectUtils
        .buildInstrumentAttachmentMetadata("issue1991", 1);
    metadata.setInstrumentId(instrumentId);
    metadata.generateId();
    metadata.setVersion(0L);

    String filename = InstrumentAttachmentFilenameBuilder.buildFileName(metadata);
    try (InputStream is = new ByteArrayInputStream("Test".getBytes(StandardCharsets.UTF_8))) {
      gridFsOperations.store(is, filename, "text/plain", metadata);
    }

    mockMvc.perform(put("/api/instruments/" + instrumentId + "/attachments/" + metadata.getFileName())
        .content(TestUtil.convertObjectToJsonBytes(metadata))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].message", containsString("global.error.shadow-update-not-allowed")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testDeleteAllAttachmentsOfShadowCopyInstrument() throws Exception {
    String instrumentId = "ins-issue1991-ins1$-1.0.0";

    InstrumentAttachmentMetadata metadata = UnitTestCreateDomainObjectUtils
        .buildInstrumentAttachmentMetadata("issue1991", 1);
    metadata.setInstrumentId(instrumentId);
    metadata.generateId();

    try (InputStream is = new ByteArrayInputStream("Test".getBytes(StandardCharsets.UTF_8))) {
      String filename = InstrumentAttachmentFilenameBuilder.buildFileName(metadata);
      gridFsOperations.store(is, filename, "text/plain", metadata);
    }

    mockMvc.perform(delete("/api/instruments/" + instrumentId + "/attachments"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].message", containsString("global.error.shadow-delete-not-allowed")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testDeleteAttachmentOfShadowCopyInstrument() throws Exception {
    String instrumentId = "ins-issue1991-ins1$-1.0.0";

    InstrumentAttachmentMetadata metadata = UnitTestCreateDomainObjectUtils
        .buildInstrumentAttachmentMetadata("issue1991", 1);
    metadata.setInstrumentId(instrumentId);
    metadata.generateId();

    String filename = InstrumentAttachmentFilenameBuilder.buildFileName(metadata);
    try (InputStream is = new ByteArrayInputStream("Test".getBytes(StandardCharsets.UTF_8))) {
      gridFsOperations.store(is, filename, "text/plain", metadata);
    }

    mockMvc.perform(delete("/api/instruments/" + instrumentId + "/attachments/" + metadata.getFileName()))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].message", containsString("global.error.shadow-delete-not-allowed")));
  }
}

package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.rest;

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

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.rest.TestUtil;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.InstrumentAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.InstrumentAttachmentTypes;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository.InstrumentRepository;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.service.InstrumentAttachmentService;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

public class InstrumentAttachmentResourceTest extends AbstractTest {
  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private InstrumentAttachmentService instrumentAttachmentService;
  
  @Autowired
  private InstrumentRepository instrumentRepository;
  
  @Autowired
  private ElasticsearchUpdateQueueItemRepository elasticsearchUpdateQueueItemRepository;
  
  @Autowired
  private JaversService javersService;

  private MockMvc mockMvc;

  @Before
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
      .build();
  }

  @After
  public void cleanUp() {
    this.instrumentRepository.deleteAll();
    this.instrumentAttachmentService.deleteAll();
    this.elasticsearchUpdateQueueItemRepository.deleteAll();
    this.javersService.deleteAll();
  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER, username="test")
  public void testUploadValidAttachment() throws Exception {

    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    InstrumentAttachmentMetadata instrumentAttachmentMetadata = UnitTestCreateDomainObjectUtils
      .buildInstrumentAttachmentMetadata("projectid", 1);
    // Client uploads without id and master id
    instrumentAttachmentMetadata.setId(null);
    instrumentAttachmentMetadata.setMasterId(null);
    MockMultipartFile metadata = new MockMultipartFile("instrumentAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(instrumentAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/instruments/attachments")
      .file(attachment)
      .file(metadata))
      .andExpect(status().isCreated());

    // read the created attachment and check the version
    mockMvc.perform(
        get("/api/instruments/" + instrumentAttachmentMetadata.getInstrumentId() + "/attachments"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.[0].instrumentId", is(instrumentAttachmentMetadata.getInstrumentId())))
      .andExpect(jsonPath("$.[0].version", is(0)))
      .andExpect(jsonPath("$.[0].createdBy", is("test")))
      .andExpect(jsonPath("$.[0].lastModifiedBy", is("test")))
      .andExpect(jsonPath("$.[0].type.en", is(InstrumentAttachmentTypes.QUESTION_FLOW.getEn())));
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
  
  @Test(expected = IllegalAccessError.class)
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testChangingImmutableI18nString() {
    InstrumentAttachmentTypes.QUESTION_FLOW.setDe("hurz");
  }
}

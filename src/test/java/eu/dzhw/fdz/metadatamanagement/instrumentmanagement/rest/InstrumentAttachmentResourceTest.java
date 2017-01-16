package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.rest;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.rest.TestUtil;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestUserManagementUtils;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.InstrumentAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository.InstrumentRepository;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.service.InstrumentAttachmentService;

public class InstrumentAttachmentResourceTest extends AbstractTest {
  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private InstrumentAttachmentService instrumentAttachmentService;
  
  @Autowired
  private InstrumentRepository instrumentRepository;

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
    UnitTestUserManagementUtils.logout();;
  }

  @Test
  public void testUploadValidAttachment() throws Exception {

    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    InstrumentAttachmentMetadata instrumentAttachmentMetadata = UnitTestCreateDomainObjectUtils
      .buildInstrumentAttachmentMetadata("projectId", 1);
    MockMultipartFile metadata = new MockMultipartFile("instrumentAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(instrumentAttachmentMetadata));

    UnitTestUserManagementUtils.login("test", "test");
    mockMvc.perform(MockMvcRequestBuilders.fileUpload("/api/instruments/attachments")
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
      .andExpect(jsonPath("$.[0].lastModifiedBy", is("test")));
  }

  @Test
  public void testUploadAttachmentWithWrongType() throws Exception {

    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    InstrumentAttachmentMetadata instrumentAttachmentMetadata = UnitTestCreateDomainObjectUtils
      .buildInstrumentAttachmentMetadata("projectId", 1);
    instrumentAttachmentMetadata.getType().setDe("hurz");
    MockMultipartFile metadata = new MockMultipartFile("instrumentAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(instrumentAttachmentMetadata));

    UnitTestUserManagementUtils.login("test", "test");


    mockMvc.perform(MockMvcRequestBuilders.fileUpload("/api/instruments/attachments")
      .file(attachment)
      .file(metadata))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.errors[0].message",
          is("instrument-management.error.instrument-attachment-metadata.type.valid-type")));
  }
  
  @Test
  public void testUploadAttachmentWithMissingTitle() throws Exception {

    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    InstrumentAttachmentMetadata instrumentAttachmentMetadata = UnitTestCreateDomainObjectUtils
      .buildInstrumentAttachmentMetadata("projectId", 1);
    instrumentAttachmentMetadata.getTitle().setDe("");

    MockMultipartFile metadata = new MockMultipartFile("instrumentAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(instrumentAttachmentMetadata));

    UnitTestUserManagementUtils.login("test", "test");


    mockMvc.perform(MockMvcRequestBuilders.fileUpload("/api/instruments/attachments")
      .file(attachment)
      .file(metadata))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.errors[0].message",
          is("instrument-management.error.instrument-attachment-metadata.title.i18n-string-size")));
  }
  
  @Test
  public void testAttachmentIsDeletedWithInstrument() throws Exception {
    Instrument instrument =
        UnitTestCreateDomainObjectUtils.buildInstrument("projectId", "projectId-sy1");

    // create the instrument with the given id
    mockMvc.perform(put("/api/instruments/" + instrument.getId())
      .content(TestUtil.convertObjectToJsonBytes(instrument)))
      .andExpect(status().isCreated());
    
    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    InstrumentAttachmentMetadata instrumentAttachmentMetadata = UnitTestCreateDomainObjectUtils
      .buildInstrumentAttachmentMetadata(instrument.getDataAcquisitionProjectId(), instrument.getNumber());

    MockMultipartFile metadata = new MockMultipartFile("instrumentAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(instrumentAttachmentMetadata));

    UnitTestUserManagementUtils.login("test", "test");

    mockMvc.perform(MockMvcRequestBuilders.fileUpload("/api/instruments/attachments")
      .file(attachment)
      .file(metadata))
      .andExpect(status().isCreated());
    
    // delete the instrument by project id
    mockMvc.perform(post("/api/instruments/delete").param("dataAcquisitionProjectId", instrument.getDataAcquisitionProjectId())
      .content(TestUtil.convertObjectToJsonBytes(instrument)))
      .andExpect(status().isOk());
    
    // check if attachment has been deleted as well
    mockMvc.perform(
        get("/api/instruments/" + instrumentAttachmentMetadata.getInstrumentId() + "/attachments"))
      .andExpect(status().isOk())
      .andExpect(content().json("[]"));
  }

}

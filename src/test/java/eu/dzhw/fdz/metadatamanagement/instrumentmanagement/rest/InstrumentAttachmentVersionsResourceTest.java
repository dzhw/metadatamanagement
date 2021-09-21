package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.rest;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import eu.dzhw.fdz.metadatamanagement.common.rest.TestUtil;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.InstrumentAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.service.InstrumentAttachmentService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

@WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
public class InstrumentAttachmentVersionsResourceTest extends AbstractTest {
  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private JaversService javersService;

  @Autowired
  private GridFsOperations gridFsOperations;

  @Autowired
  private InstrumentAttachmentService instrumentAttachmentService;

  private MockMvc mockMvc;

  @BeforeEach
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
  }

  @AfterEach
  public void cleanUp() {
    javersService.deleteAll();
    this.gridFsOperations.delete(new Query());
  }

  @Test
  public void testUploadValidAttachmentAndCheckVersion() throws Exception {
    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    InstrumentAttachmentMetadata instrumentAttachmentMetadata =
        UnitTestCreateDomainObjectUtils.buildInstrumentAttachmentMetadata("projectid", 1);
    MockMultipartFile metadata = new MockMultipartFile("instrumentAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(instrumentAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/instruments/attachments")
        .file(attachment)
        .file(metadata)).andExpect(status().isCreated());

    // assert that there is one initial version
    mockMvc
        .perform(get("/api/instruments/" + instrumentAttachmentMetadata.getInstrumentId()
            + "/attachments/" + instrumentAttachmentMetadata.getFileName() + "/versions"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(1)));

    // update the metadata
    InstrumentAttachmentMetadata current = instrumentAttachmentService
        .findAllByInstrument(instrumentAttachmentMetadata.getInstrumentId()).get(0);
    current.setLanguage("en");

    mockMvc.perform(
        put("/api/instruments/" + instrumentAttachmentMetadata.getInstrumentId() + "/attachments/"
        + attachment.getOriginalFilename()).content(TestUtil.convertObjectToJsonBytes(current))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());

    // assert that there are two versions
    mockMvc
        .perform(get("/api/instruments/" + instrumentAttachmentMetadata.getInstrumentId()
            + "/attachments/" + instrumentAttachmentMetadata.getFileName() + "/versions"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(2)));
  }

  @Test
  public void testVersionsNotFound() throws Exception {
    mockMvc.perform(get("/api/instruments/spaß/attachments/file/versions"))
        .andExpect(status().isNotFound());
  }
}

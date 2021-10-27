package eu.dzhw.fdz.metadatamanagement.projectmanagement.rest;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
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
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackageAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.InstrumentAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;

public class DataAcquisitionProjectAttachmentsResourceControllerTest extends AbstractTest {
  private static final String API_DATA_ACQUISITION_PROJECTS_URI = "/api/data-acquisition-projects";
  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private JaversService javersService;

  private MockMvc mockMvc;

  @Autowired
  private GridFsOperations gridFsOperations;

  @BeforeEach
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
  }

  @AfterEach
  public void cleanUp() {
    gridFsOperations.delete(new Query());
    javersService.deleteAll();
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testFindAllAttachmentsOfDataPackage() throws IOException, Exception {
    String projectId = "projectid";
    createDataPackageAttachment(projectId);
    createInstrumentAttachment(projectId, 1);
    createInstrumentAttachment(projectId, 2);

    // assert that there are two attachments (instrument attachments deduplicated)
    mockMvc.perform(get(API_DATA_ACQUISITION_PROJECTS_URI + "/" + projectId + "/attachments"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.dataPackage.length()", is(1)))
        .andExpect(jsonPath("$.surveys.length()", is(0)))
        .andExpect(jsonPath("$.instruments.length()", is(1)))
        .andExpect(jsonPath("$.dataSets.length()", is(0)));
  }

  private void createDataPackageAttachment(String projectId) throws Exception {
    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    DataPackageAttachmentMetadata dataPackageAttachmentMetadata =
        UnitTestCreateDomainObjectUtils.buildDataPackageAttachmentMetadata(projectId);
    MockMultipartFile metadata = new MockMultipartFile("dataPackageAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(dataPackageAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/data-packages/attachments")
        .file(attachment).file(metadata)).andExpect(status().isCreated());
  }

  private void createInstrumentAttachment(String projectId, Integer instrumentNumber)
      throws Exception {
    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    InstrumentAttachmentMetadata instrumentAttachmentMetadata =
        UnitTestCreateDomainObjectUtils.buildInstrumentAttachmentMetadata(projectId,
            instrumentNumber);
    MockMultipartFile metadata = new MockMultipartFile("instrumentAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(instrumentAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/instruments/attachments")
        .file(attachment).file(metadata)).andExpect(status().isCreated());
  }
}

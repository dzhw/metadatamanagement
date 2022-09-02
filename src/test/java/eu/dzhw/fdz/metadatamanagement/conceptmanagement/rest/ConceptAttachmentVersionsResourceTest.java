package eu.dzhw.fdz.metadatamanagement.conceptmanagement.rest;

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
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.ConceptAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.service.ConceptAttachmentService;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;

@WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
public class ConceptAttachmentVersionsResourceTest extends AbstractTest {
  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private JaversService javersService;

  @Autowired
  private GridFsOperations gridFsOperations;

  @Autowired
  private ConceptAttachmentService conceptAttachmentService;

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
    ConceptAttachmentMetadata conceptAttachmentMetadata =
        UnitTestCreateDomainObjectUtils.buildConceptAttachmentMetadata("conceptid");
    MockMultipartFile metadata = new MockMultipartFile("conceptAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(conceptAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/concepts/attachments")
        .file(attachment)
        .file(metadata)).andExpect(status().isCreated());

    // assert that there is one initial version
    mockMvc
        .perform(get("/api/concepts/" + conceptAttachmentMetadata.getConceptId() + "/attachments/"
            + conceptAttachmentMetadata.getFileName() + "/versions"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(1)));

    // update the metadata
    ConceptAttachmentMetadata current =
        conceptAttachmentService.findAllByConcept(conceptAttachmentMetadata.getConceptId()).get(0);
    current.setLanguage("en");

    mockMvc.perform(
        put("/api/concepts/" + conceptAttachmentMetadata.getConceptId() + "/attachments/"
        + attachment.getOriginalFilename()).content(TestUtil.convertObjectToJsonBytes(current))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());

    // assert that there are two versions
    mockMvc
        .perform(get("/api/concepts/" + conceptAttachmentMetadata.getConceptId() + "/attachments/"
            + conceptAttachmentMetadata.getFileName() + "/versions"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(2)));
  }

  @Test
  public void testVersionsNotFound() throws Exception {
    mockMvc.perform(get("/api/concepts/spaß/attachments/file/versions"))
        .andExpect(status().isNotFound());
  }
}

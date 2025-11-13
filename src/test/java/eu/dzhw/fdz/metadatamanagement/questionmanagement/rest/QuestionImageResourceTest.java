package eu.dzhw.fdz.metadatamanagement.questionmanagement.rest;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

import com.mongodb.client.gridfs.model.GridFSFile;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.rest.TestUtil;
import eu.dzhw.fdz.metadatamanagement.common.service.GridFsMetadataUpdateService;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.QuestionImageMetadata;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

@Disabled
public class QuestionImageResourceTest extends AbstractTest {

  @Autowired
  private WebApplicationContext wac;

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

  private MockMvc mockMvc;

  @BeforeEach
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
  }

  @AfterEach
  public void cleanUp() {
    this.elasticsearchUpdateQueueItemRepository.deleteAll();
    this.javersService.deleteAll();
    this.gridFsOperations.delete(new Query());
    this.elasticsearchAdminService.recreateAllIndices();
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testUploadQuestionImageResource() throws Exception {
    MockMultipartFile attachment =
        new MockMultipartFile("image", "image.png", "image/png", "fakeimage".getBytes());
    QuestionImageMetadata questionImageMetadata =
        UnitTestCreateDomainObjectUtils.buildQuestionImageMetadata("projectid", "questionid");
    MockMultipartFile metadata = new MockMultipartFile("questionImageMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(questionImageMetadata));

    mockMvc
        .perform(MockMvcRequestBuilders.multipart("/api/questions/images").file(attachment)
            .file(metadata))
        .andExpect(status().isCreated());

    mockMvc.perform(get("/api/questions/" + questionImageMetadata.getQuestionId() + "/images"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(1)));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testCreateShadowCopyQuestionImageMetadata() throws Exception {
    MockMultipartFile attachment =
        new MockMultipartFile("image", "image.png", "image/png", "fakeimage".getBytes());
    QuestionImageMetadata questionImageMetadata =
        UnitTestCreateDomainObjectUtils.buildQuestionImageMetadata("projectid", "questionid");
    questionImageMetadata.setQuestionId(questionImageMetadata.getQuestionId() + "-1.0.0");
    questionImageMetadata.generateId();
    MockMultipartFile metadata = new MockMultipartFile("questionImageMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(questionImageMetadata));

    mockMvc
        .perform(MockMvcRequestBuilders.multipart("/api/questions/images").file(attachment)
            .file(metadata))
        .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors[0].message",
            containsString("global.error.shadow-create-not-allowed")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testUploadEmptyImage() throws Exception {
    MockMultipartFile attachment =
        new MockMultipartFile("image", "image.png", "image/png", "".getBytes());
    QuestionImageMetadata questionImageMetadata =
        UnitTestCreateDomainObjectUtils.buildQuestionImageMetadata("projectid", "questionid");
    questionImageMetadata.setQuestionId(questionImageMetadata.getQuestionId() + "-1.0.0");
    questionImageMetadata.generateId();
    MockMultipartFile metadata = new MockMultipartFile("questionImageMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(questionImageMetadata));

    // create the image
    mockMvc.perform(
        MockMvcRequestBuilders.multipart("/api/questions/images").file(attachment).file(metadata))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testDeleteAllQuestionImagessOfShadowCopyQuestion() throws Exception {
    String questionId = "ins-issue1991-ins1$-1.0.0";

    QuestionImageMetadata metadata =
        UnitTestCreateDomainObjectUtils.buildQuestionImageMetadata("issue1991", "questionid");
    metadata.setQuestionId(questionId);
    metadata.generateId();

    String filename =
        String.format("/questions/%s/images/%s", metadata.getQuestionId(), metadata.getFileName());
    try (InputStream is = new ByteArrayInputStream("Test".getBytes(StandardCharsets.UTF_8))) {
      gridFsMetadataUpdateService.store(is, filename, "text/plain", metadata);
    }

    mockMvc.perform(delete("/api/questions/" + questionId + "/images"))
        .andExpect(status().isNoContent());

    Iterator<GridFSFile> iterator = gridFsOperations.find(new Query()).iterator();

    assertThat(iterator.hasNext(), equalTo(true));
    assertThat(iterator.next().getFilename(), equalTo(filename));
  }
}

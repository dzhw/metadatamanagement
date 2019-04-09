package eu.dzhw.fdz.metadatamanagement.datasetmanagement.rest;

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
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSetAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.service.DataSetAttachmentFilenameBuilder;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

public class DataSetAttachmentResourceTest extends AbstractTest {
  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private DataSetRepository dataSetRepository;

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
    this.dataSetRepository.deleteAll();
    this.elasticsearchUpdateQueueItemRepository.deleteAll();
    this.javersService.deleteAll();
    gridFs.getFileList().iterator().forEachRemaining(gridFs::remove);
  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER, username="test")
  public void testUploadValidAttachment() throws Exception {

    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    DataSetAttachmentMetadata dataSetAttachmentMetadata = UnitTestCreateDomainObjectUtils
      .buildDataSetAttachmentMetadata("projectid", 1);
    // Client uploads without id and masterId
    dataSetAttachmentMetadata.setId(null);
    dataSetAttachmentMetadata.setMasterId(null);
    MockMultipartFile metadata = new MockMultipartFile("dataSetAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(dataSetAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/data-sets/attachments")
      .file(attachment)
      .file(metadata))
      .andExpect(status().isCreated());

    dataSetAttachmentMetadata.generateId();

    // read the created attachment and check the version
    mockMvc.perform(
        get("/api/data-sets/" + dataSetAttachmentMetadata.getDataSetId() + "/attachments"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.[0].dataSetId", is(dataSetAttachmentMetadata.getDataSetId())))
      .andExpect(jsonPath("$.[0].version", is(0)))
      .andExpect(jsonPath("$.[0].createdBy", is("test")))
      .andExpect(jsonPath("$.[0].lastModifiedBy", is("test")))
      .andExpect(jsonPath("$.[0].masterId", is(dataSetAttachmentMetadata.getId())));
  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testUploadAttachmentWithWrongLanguage() throws Exception {

    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    DataSetAttachmentMetadata dataSetAttachmentMetadata = UnitTestCreateDomainObjectUtils
      .buildDataSetAttachmentMetadata("projectid", 1);
    dataSetAttachmentMetadata.setLanguage("test");
    MockMultipartFile metadata = new MockMultipartFile("dataSetAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(dataSetAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/data-sets/attachments")
      .file(attachment)
      .file(metadata))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.errors[0].message",
          is("data-set-management.error.data-set-attachment-metadata.language.not-supported")));
  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testUploadAttachmentWithMissingDescription() throws Exception {

    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    DataSetAttachmentMetadata dataSetAttachmentMetadata = UnitTestCreateDomainObjectUtils
      .buildDataSetAttachmentMetadata("projectid", 1);
    dataSetAttachmentMetadata.setDescription(new I18nString());

    MockMultipartFile metadata = new MockMultipartFile("dataSetAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(dataSetAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/data-sets/attachments")
      .file(attachment)
      .file(metadata))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.errors[0].message",
          is("data-set-management.error.data-set-attachment-metadata.description.i18n-string-not-empty")));
  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testAttachmentIsDeletedWithDataSet() throws Exception {
    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey("projectId");
    DataSet dataSet =
        UnitTestCreateDomainObjectUtils.buildDataSet("projectid", survey.getId() , survey.getNumber());

    // create the dataSet with the given id
    mockMvc.perform(put("/api/data-sets/" + dataSet.getId())
      .content(TestUtil.convertObjectToJsonBytes(dataSet)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated());

    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    DataSetAttachmentMetadata dataSetAttachmentMetadata = UnitTestCreateDomainObjectUtils
      .buildDataSetAttachmentMetadata(dataSet.getDataAcquisitionProjectId(), dataSet.getNumber());

    MockMultipartFile metadata = new MockMultipartFile("dataSetAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(dataSetAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/data-sets/attachments")
      .file(attachment)
      .file(metadata))
      .andExpect(status().isCreated());

    // delete the dataSet with the given id
    mockMvc.perform(delete("/api/data-sets/" + dataSet.getId()))
      .andExpect(status().isNoContent());

    // check if attachment has been deleted as well
    mockMvc.perform(
        get("/api/data-sets/" + dataSetAttachmentMetadata.getDataSetId() + "/attachments"))
      .andExpect(status().isOk())
      .andExpect(content().json("[]"));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void createShadowCopyDataSetAttachment() throws Exception {
    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    DataSetAttachmentMetadata dataSetAttachmentMetadata = UnitTestCreateDomainObjectUtils
        .buildDataSetAttachmentMetadata("issue1991", 1);
    dataSetAttachmentMetadata.setDataSetId(dataSetAttachmentMetadata.getDataSetId() + "-1.0.0");
    dataSetAttachmentMetadata.generateId();

    MockMultipartFile metadata = new MockMultipartFile("dataSetAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(dataSetAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/data-sets/attachments")
        .file(attachment)
        .file(metadata))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].message", containsString("global.error.shadow-create-not-allowed")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testUpdateAttachmentOfShadowDataSet() throws Exception {
    String dataSetId = "dat-issue1991-ds1$-1.0.0";
    DataSetAttachmentMetadata metadata = UnitTestCreateDomainObjectUtils
        .buildDataSetAttachmentMetadata("issue1991", 1);
    metadata.setDataSetId(dataSetId);
    metadata.generateId();
    metadata.setVersion(0L);

    String filename = DataSetAttachmentFilenameBuilder.buildFileName(metadata);
    try (InputStream is = new ByteArrayInputStream("Test".getBytes(StandardCharsets.UTF_8))) {
      gridFsOperations.store(is, filename, "text/plain", metadata);
    }

    mockMvc.perform(put("/api/data-sets/" + dataSetId + "/attachments/" + metadata.getFileName())
        .content(TestUtil.convertObjectToJsonBytes(metadata))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].message", containsString("global.error.shadow-update-not-allowed")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testDeleteAllAttachmentsOfShadowCopyDataSet() throws Exception {
    String dataSetId = "dat-issue1991-ds1$-1.0.0";

    DataSetAttachmentMetadata metadata = UnitTestCreateDomainObjectUtils
        .buildDataSetAttachmentMetadata("issue1991", 1);
    metadata.setDataSetId(dataSetId);
    metadata.generateId();

    try (InputStream is = new ByteArrayInputStream("Test".getBytes(StandardCharsets.UTF_8))) {
      String filename = DataSetAttachmentFilenameBuilder.buildFileName(metadata);
      gridFsOperations.store(is, filename, "text/plain", metadata);
    }

    mockMvc.perform(delete("/api/data-sets/" + dataSetId + "/attachments"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].message", containsString("global.error.shadow-delete-not-allowed")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testDeleteAttachmentOfShadowDataSet() throws Exception {
    String dataSetId = "dat-issue1991-ds1$-1.0.0";

    DataSetAttachmentMetadata metadata = UnitTestCreateDomainObjectUtils
        .buildDataSetAttachmentMetadata("issue1991", 1);
    metadata.setDataSetId(dataSetId);
    metadata.generateId();

    String filename = DataSetAttachmentFilenameBuilder.buildFileName(metadata);
    try (InputStream is = new ByteArrayInputStream("Test".getBytes(StandardCharsets.UTF_8))) {
      gridFsOperations.store(is, filename, "text/plain", metadata);
    }

    mockMvc.perform(delete("/api/data-sets/" + dataSetId + "/attachments/" + metadata.getFileName()))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].message", containsString("global.error.shadow-delete-not-allowed")));
  }
}

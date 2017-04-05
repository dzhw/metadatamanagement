package eu.dzhw.fdz.metadatamanagement.datasetmanagement.rest;

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
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSetAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.service.DataSetAttachmentService;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

public class DataSetAttachmentResourceTest extends AbstractTest {
  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private DataSetAttachmentService dataSetAttachmentService;
  
  @Autowired
  private DataSetRepository dataSetRepository;

  private MockMvc mockMvc;

  @Before
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
      .build();
  }

  @After
  public void cleanUp() {
    this.dataSetRepository.deleteAll();
    this.dataSetAttachmentService.deleteAll();
  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER, username="test")
  public void testUploadValidAttachment() throws Exception {

    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    DataSetAttachmentMetadata dataSetAttachmentMetadata = UnitTestCreateDomainObjectUtils
      .buildDataSetAttachmentMetadata("projectId", 1);
    MockMultipartFile metadata = new MockMultipartFile("dataSetAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(dataSetAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.fileUpload("/api/data-sets/attachments")
      .file(attachment)
      .file(metadata))    
      .andExpect(status().isCreated());

    // read the created attachment and check the version
    mockMvc.perform(
        get("/api/data-sets/" + dataSetAttachmentMetadata.getDataSetId() + "/attachments"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.[0].dataSetId", is(dataSetAttachmentMetadata.getDataSetId())))
      .andExpect(jsonPath("$.[0].version", is(0)))
      .andExpect(jsonPath("$.[0].createdBy", is("test")))
      .andExpect(jsonPath("$.[0].lastModifiedBy", is("test")));
  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testUploadAttachmentWithWrongLanguage() throws Exception {

    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    DataSetAttachmentMetadata dataSetAttachmentMetadata = UnitTestCreateDomainObjectUtils
      .buildDataSetAttachmentMetadata("projectId", 1);
    dataSetAttachmentMetadata.setLanguage("test");
    MockMultipartFile metadata = new MockMultipartFile("dataSetAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(dataSetAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.fileUpload("/api/data-sets/attachments")
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
      .buildDataSetAttachmentMetadata("projectId", 1);
    dataSetAttachmentMetadata.setDescription(new I18nString());

    MockMultipartFile metadata = new MockMultipartFile("dataSetAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(dataSetAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.fileUpload("/api/data-sets/attachments")
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
        UnitTestCreateDomainObjectUtils.buildDataSet("projectId", survey.getId() , survey.getNumber());

    // create the dataSet with the given id
    mockMvc.perform(put("/api/data-sets/" + dataSet.getId())
      .content(TestUtil.convertObjectToJsonBytes(dataSet)))
      .andExpect(status().isCreated());
    
    MockMultipartFile attachment =
        new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
    DataSetAttachmentMetadata dataSetAttachmentMetadata = UnitTestCreateDomainObjectUtils
      .buildDataSetAttachmentMetadata(dataSet.getDataAcquisitionProjectId(), dataSet.getNumber());

    MockMultipartFile metadata = new MockMultipartFile("dataSetAttachmentMetadata", "Blob",
        "application/json", TestUtil.convertObjectToJsonBytes(dataSetAttachmentMetadata));

    mockMvc.perform(MockMvcRequestBuilders.fileUpload("/api/data-sets/attachments")
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

}

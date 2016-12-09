package eu.dzhw.fdz.metadatamanagement.datasetmanagement.rest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

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
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import eu.dzhw.fdz.metadatamanagement.filemanagement.service.FileService;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;

/**
 * @author Daniel Katzberg
 *
 */
public class DataSetsReportResourceTest extends AbstractTest {

  private static final String API_DATASETS_REPORTS_URI = "/api/data-sets/report";

  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;

  @Autowired
  private DataSetRepository dataSetRepository;

  @Autowired
  private FileService fileService;

  private MockMvc mockMvc;

  @Before
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
      .build();
  }

  @After
  public void cleanUp() {
    this.dataAcquisitionProjectRepository.deleteAll();
    this.dataSetRepository.deleteAll();
    this.fileService.deleteTempFiles();
  }

  //TODO DKatzberg @Test
  public void testValidUpload() throws Exception {

    // Arrange
    Path currentRelativePath = Paths.get("");
    String basicPath = currentRelativePath.toAbsolutePath()
      .toString();
    File templatePath = new File(basicPath + "/src/test/resources/data/latexExample/");

    FileInputStream fileInputStream = new FileInputStream(templatePath + "/TemplateExample.zip");
    byte[] texTemplate = new byte[fileInputStream.available()];
    fileInputStream.read(texTemplate);
    fileInputStream.close();

    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    this.dataAcquisitionProjectRepository.save(project);

    DataSet dataSet = UnitTestCreateDomainObjectUtils.buildDataSet(project.getId(), null);
    this.dataSetRepository.save(dataSet);

    // Act and Assert
    MockMultipartFile multipartFile =
        new MockMultipartFile("file", "TemplateExample.zip", "application/zip", texTemplate);

    this.mockMvc.perform(MockMvcRequestBuilders.fileUpload(API_DATASETS_REPORTS_URI)
      .file(multipartFile)
      .param("id", dataSet.getId()))
      .andExpect(status().isOk());
  }

  @Test
  public void testNonValidUpload() throws Exception {

    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    this.dataAcquisitionProjectRepository.save(project);
    byte[] empty = new byte[0];

    DataSet dataSet = UnitTestCreateDomainObjectUtils.buildDataSet(project.getId(), null);
    this.dataSetRepository.save(dataSet);

    // Act and Assert
    MockMultipartFile multipartFile = new MockMultipartFile("file", empty);
    this.mockMvc.perform(MockMvcRequestBuilders.fileUpload(API_DATASETS_REPORTS_URI)
      .file(multipartFile)
      .param("id", dataSet.getId()))
      .andExpect(status().isBadRequest());
  }
}

package eu.dzhw.fdz.metadatamanagement.filemanagement.rest;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.test.web.servlet.MvcResult;
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
public class FileResourceTest extends AbstractTest {
  private static final String API_DATASETS_REPORTS_URI = "/api/data-sets/report";
  private static final String PUBLIC_FILES_URI = "/public/files";

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

  @Test
  public void testValidUpload() throws Exception {

    // Arrange
    Path currentRelativePath = Paths.get("");
    String basicPath = currentRelativePath.toAbsolutePath()
      .toString();
    File templatePath = new File(basicPath + "/src/test/resources/data/latexExample/");

    FileInputStream fileInputStream = new FileInputStream(templatePath + "/ExampleTexTemplate.tex");
    byte[] texTemplate = new byte[(int) templatePath.length()];
    fileInputStream.read(texTemplate);
    fileInputStream.close();

    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    this.dataAcquisitionProjectRepository.save(project);

    DataSet dataSet = UnitTestCreateDomainObjectUtils.buildDataSet(project.getId(), null);
    dataSet.getVariableIds()
      .clear();
    this.dataSetRepository.save(dataSet);

    // Act and Assert Upload File
    MockMultipartFile multipartFile = new MockMultipartFile("file", texTemplate);
    MvcResult mvcResultUpload =
        this.mockMvc.perform(MockMvcRequestBuilders.fileUpload(API_DATASETS_REPORTS_URI)
          .file(multipartFile)
          .param("id", dataSet.getId()))
          .andExpect(status().isOk())
          .andReturn();

    String texTemplateNameInGridFS = mvcResultUpload.getResponse()
      .getContentAsString();

    // Act and Assert Download
    MvcResult mvcResultDownload =
        this.mockMvc.perform(get(PUBLIC_FILES_URI + '/' + texTemplateNameInGridFS))
          .andExpect(status().isOk())
          .andReturn();

    String filledTexFile = new String(mvcResultDownload.getResponse()
      .getContentAsByteArray());

    assertThat(filledTexFile.contains("documentclass"), is(true));
  }
}

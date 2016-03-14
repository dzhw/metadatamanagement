package eu.dzhw.fdz.metadatamanagement.web.rest;

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
import eu.dzhw.fdz.metadatamanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.service.reporter.LatexTemplateService;
import eu.dzhw.fdz.metadatamanagement.unittest.util.UnitTestCreateDomainObjectUtils;

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
  private LatexTemplateService latexTemplateService;

  private MockMvc mockMvc;

  @Before
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
      .build();
  }

  @After
  public void cleanUp() {
    this.dataAcquisitionProjectRepository.deleteAll();
    this.latexTemplateService.deleteTexTemplates();
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

    // Act and Assert Upload File
    MockMultipartFile multipartFile = new MockMultipartFile("file", texTemplate);
    MvcResult mvcResultUpload =
        this.mockMvc.perform(MockMvcRequestBuilders.fileUpload(API_DATASETS_REPORTS_URI)
          .file(multipartFile)
          .param("id", project.getId()))
          .andExpect(status().isOk())
          .andReturn();

    String texTemplateNameInGridFS = mvcResultUpload.getResponse()
      .getContentAsString();

    // Act and Assert Download
    MvcResult mvcResultDownload =
        this.mockMvc.perform(get(PUBLIC_FILES_URI + "/" + texTemplateNameInGridFS))
          .andExpect(status().isOk())
          .andReturn();

    String filledTexFile = mvcResultDownload.getResponse()
      .getContentAsString();

    assertThat(filledTexFile.contains(project.getId()), is(true));
  }
}

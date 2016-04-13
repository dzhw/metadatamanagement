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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.filemanagement.service.FileService;

/**
 * @author Daniel Katzberg
 *
 */
public class FileResourceTest extends AbstractTest {
  private static final String PUBLIC_FILES_URI = "/public/files";

  @Autowired
  private WebApplicationContext wac;

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
    String namePath =
        this.fileService.saveTempFile(fileInputStream, "TestName", "application/x-tex");

    // Act and Assert Download
    MvcResult mvcResultDownload =
        this.mockMvc.perform(get(PUBLIC_FILES_URI + namePath))
          .andExpect(status().isOk())
          .andReturn();

    String texTemplateDownload = new String(mvcResultDownload.getResponse()
      .getContentAsByteArray());

    assertThat(texTemplateDownload.contains("documentclass"), is(true));
  }
}

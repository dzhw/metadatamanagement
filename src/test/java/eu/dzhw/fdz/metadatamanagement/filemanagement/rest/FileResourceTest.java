package eu.dzhw.fdz.metadatamanagement.filemanagement.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
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
  
  @Autowired
  private JaversService javersService;

  private MockMvc mockMvc;

  @BeforeEach
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
      .build();
  }

  @AfterEach
  public void cleanUp() {
    this.fileService.deleteTempFiles();
    this.javersService.deleteAll();
  }

  @Test
  public void testValidUpload() throws Exception {

    // Arrange
    Path currentRelativePath = Paths.get("");
    String basicPath = currentRelativePath.toAbsolutePath()
      .toString();
    File templatePath = new File(basicPath + "/src/test/resources/data/latexExample/");
    FileInputStream fileInputStream = new FileInputStream(templatePath + "/TemplateExample.zip");
    String namePath =
        this.fileService.saveTempFile(fileInputStream, "TestName", "application/zip");

    // Act and Assert Download
    this.mockMvc.perform(get(PUBLIC_FILES_URI + namePath))
          .andExpect(status().isOk())
          .andReturn();
  }
}

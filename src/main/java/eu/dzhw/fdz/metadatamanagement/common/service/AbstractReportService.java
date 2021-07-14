package eu.dzhw.fdz.metadatamanagement.common.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.javers.common.collections.Lists;
import org.springframework.scheduling.annotation.Async;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import eu.dzhw.fdz.metadatamanagement.common.domain.Task;
import eu.dzhw.fdz.metadatamanagement.common.rest.util.ZipUtil;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.service.DataPackageOverviewService;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.exception.TemplateIncompleteException;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.service.DataSetReportService;
import eu.dzhw.fdz.metadatamanagement.filemanagement.service.FileService;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.AccessWays;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Base class for generating reports like {@link DataSetReportService} and
 * {@link DataPackageOverviewService}.
 * 
 * It configures freemarker and executes it. The Model for the templates must be provided by the
 * concrete sub classes.
 */
@RequiredArgsConstructor
@Slf4j
public abstract class AbstractReportService {
  protected final FileService fileService;

  protected final TaskManagementService taskService;

  protected final MarkdownHelper markdownHelper;

  /**
   * The Escape Prefix handles the escaping of special latex signs within data information. This
   * Prefix will be copied before the template source code.
   */
  protected static final String ESCAPE_PREFIX =
      "<#escape x as x?replace(\"\\\\\", \"\\\\textbackslash{}\")"
          + "?replace(\"{\", \"\\\\{\")?replace(\"}\", \"\\\\}\")"
          + "?replace(\"#\", \"\\\\#\")?replace(\"$\", \"\\\\$\")"
          + "?replace(\"%\", \"\\\\%\")?replace(\"&\", \"\\\\&\")"
          + "?replace(\"^\", \"\\\\textasciicircum{}\")?replace(\"_\", \"\\\\_\")"
          + "?replace(\">\", \"\\\\textgreater{}\")?replace(\"<\", \"\\\\textless{}\")"
          + "?replace(\"~\", \"\\\\textasciitilde{}\")" + "?replace(\"\\r\\n\", \"\\\\par  \")"
          + "?replace(\"\\n\", \"\\\\par  \")>";

  /**
   * The Escape Suffix closes the escaping prefix. This Prefix will be copied after the template
   * source code.
   */
  protected static final String ESCAPE_SUFFIX = "</#escape>";

  /**
   * Zip Mime Content Type.
   */
  protected static final String CONTENT_TYPE_ZIP = "application/zip";

  /**
   * Get all filenames expected to be present in the template zip.
   * 
   * @return List of expected filenames
   */
  private List<String> getExpectedFiles() {
    return Lists.join(getSimpleTemplateFiles(), getComplexTemplateFiles());
  }

  /**
   * Get all filenames of template which can be simply processed.
   * 
   * @return List of simple to process files.
   */
  protected abstract List<String> getSimpleTemplateFiles();

  /**
   * Get all filenames of template which need to be processed in a more complex way (like
   * "Variable.tex").
   * 
   * @return List of simple to process files.
   */
  protected abstract List<String> getComplexTemplateFiles();

  /**
   * This method load all needed objects from the db for filling the tex template.
   *
   * @param id the id of the dataset or data package.
   * @param version The version of the report as it is displayed in the title.
   * @return A HashMap with all data for the template filling. The Key is the name of the Object,
   *         which is used in the template.
   */
  protected abstract Map<String, Object> loadDataForTemplateFilling(String id, String version);

  /**
   * Checks for all files which are included for the tex template.
   *
   * @param zipFileSystem The zip file as file system
   * @return True if all files are included. False min one file is missing.
   */
  private List<String> validateZipStructure(FileSystem zipFileSystem) {
    List<String> missingTexFiles = new ArrayList<>();

    this.getExpectedFiles().forEach(filename -> {
      Path file = zipFileSystem.getPath(zipFileSystem.getPath("/").toString(), filename);
      if (!Files.exists(file)) {
        missingTexFiles.add(filename);
      }
    });
    return missingTexFiles;
  }

  /**
   * This method save a latex file into GridFS/MongoDB based on a byteArrayOutputStream.
   *
   * @param fileName The name of the file to be saved
   * @return return the file name of the saved latex template in the GridFS / MongoDB.
   * @throws IOException thrown if a stream cannot be closed
   */
  @SuppressFBWarnings("OBL_UNSATISFIED_OBLIGATION")
  private String saveCompleteZipFile(File zipFile, String fileName) throws IOException {
    // No Update by API, so we have to delete first.
    fileService.deleteTempFile(fileName);
    // Save tex file
    return fileService.saveTempFile(new FileInputStream(zipFile), fileName, CONTENT_TYPE_ZIP);
  }

  /**
   * This method fills the tex templates.
   *
   * @param templateContent The content of a tex template.
   * @param templateConfiguration The configuration for freemarker.
   * @param fileName filename of the script which will be filled in this method.
   * @return The filled tex templates as byte array.
   * @throws IOException Handles IO Exception.
   * @throws TemplateException Handles template Exceptions.
   */
  protected final String fillTemplate(String templateContent, Configuration templateConfiguration,
      Map<String, Object> dataForTemplate, String fileName) throws IOException, TemplateException {
    String templateName = "texTemplate";
    if (fileName != null && fileName.trim().length() > 0) {
      templateName = fileName;
    }

    // Read Template and escape elements
    Template texTemplate = new Template(templateName,
        ESCAPE_PREFIX + templateContent + ESCAPE_SUFFIX, templateConfiguration);

    try (Writer stringWriter = new StringWriter()) {
      texTemplate.process(dataForTemplate, stringWriter);

      stringWriter.flush();
      return stringWriter.toString();
    }
  }

  /**
   * This service method will receive a tex template as a string and an id of a data set. With this
   * id, the service will load the data set for receiving all depending information, which are
   * needed for filling of the tex template with data.
   *
   * @param zipTmpFilePath The path to uploaded zip file
   * @param originalName the original name of multipartfile
   * @param id An id of the dataset or data package.
   * @param task the task to update the status of the pro
   * @param version The version of the report as it is displayed in the title.
   *
   * @throws TemplateException Handles templates exceptions.
   * @throws IOException Handles IO Exception for the template.
   */
  @Async
  public void generateReport(Path zipTmpFilePath, String originalName, String id, Task task,
      String version) {
    log.debug("Start generating report/overview for {} and id {}", originalName, id);
    // Configuration, based on Freemarker Version 2.3.23
    Configuration templateConfiguration = new Configuration(Configuration.VERSION_2_3_23);
    templateConfiguration.setDefaultEncoding(StandardCharsets.UTF_8.toString());
    templateConfiguration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    templateConfiguration.setNumberFormat("0.######");
    URI uriOfZipFile = URI.create("jar:" + zipTmpFilePath.toUri());
    // Prepare Zip enviroment config
    Map<String, String> env = new HashMap<>();
    env.put("create", "true");
    env.put("encoding", StandardCharsets.UTF_8.name());
    try (FileSystem zipFileSystem = FileSystems.newFileSystem(uriOfZipFile, env);) {
      // Check missing files.
      log.debug("Check missing files.");
      List<String> missingTexFiles = this.validateZipStructure(zipFileSystem);
      if (!missingTexFiles.isEmpty()) {
        String message = "common.error" + ".files-in-template-zip-incomplete";
        log.debug(message + missingTexFiles);
        throw new TemplateIncompleteException(message, missingTexFiles);
      }
      // Load data for template only once
      Map<String, Object> dataForTemplate = this.loadDataForTemplateFilling(id, version);

      dataForTemplate.put("removeMarkdown", markdownHelper.createRemoveMarkdownMethod());
      dataForTemplate.put("displayAccessWay", AccessWays.createDisplayAccessWayMethod());

      for (String filename : getSimpleTemplateFiles()) {
        String template = ZipUtil.readFileFromZip(zipFileSystem.getPath(filename));
        String filledTemplate =
            this.fillTemplate(template, templateConfiguration, dataForTemplate, filename);
        ZipUtil.writeFileToZip(zipFileSystem.getPath(filename), filledTemplate);
      }

      // Create Variables pages
      for (String filename : getComplexTemplateFiles()) {
        handleComplexTemplateFile(filename, zipFileSystem, dataForTemplate, templateConfiguration);
      }

      // Save into MongoDB / GridFS
      zipFileSystem.close();
      File zipTmpFile = zipTmpFilePath.toFile();
      String fileName = this.saveCompleteZipFile(zipTmpFile, originalName);
      log.debug("file saved, start #handletaskDone");
      taskService.handleTaskDone(task, fileName);
    } catch (RuntimeException e) {
      log.error("failed generating report", e);
      taskService.handleErrorTask(task, e);
      throw e;
    } catch (Exception e) {
      log.error("failed generating report", e);
      taskService.handleErrorTask(task, e);
    }
  }

  protected void handleComplexTemplateFile(String templateFilename, FileSystem zipFileSystem,
      Map<String, Object> dataForTemplate, Configuration templateConfiguration)
      throws IOException, TemplateException {}
}

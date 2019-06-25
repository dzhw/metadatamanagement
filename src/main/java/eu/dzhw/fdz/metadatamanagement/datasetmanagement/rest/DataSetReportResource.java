package eu.dzhw.fdz.metadatamanagement.datasetmanagement.rest;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import eu.dzhw.fdz.metadatamanagement.common.domain.Task;
import eu.dzhw.fdz.metadatamanagement.common.service.TaskService;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.exception.TemplateIncompleteException;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.service.DataSetReportService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import freemarker.template.TemplateException;

/**
 * This Resource handles the filling of the tex template and the upload of the compiled file.
 */
@Controller
@RequestMapping("/api")
public class DataSetReportResource {

  @Autowired
  private DataSetReportService dataSetReportService;

  @Autowired
  private TaskService taskService;

  /**
   * Fill the given zip file which contains latex templates.
   * 
   * @param templateZip The latex template as multipart file
   * @param dataSetId the id of the data set, for which the template needs to be processed
   * @param version The version of the dataset report as it is displayed on the first page
   * @throws IOException Handles io exception for the template. (Freemarker Templates)
   * @throws TemplateException Handles template exceptions. (Freemarker Templates)
   */
  @PostMapping(value = "/data-sets/fill-template")
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public ResponseEntity<Task> fillTemplate(@RequestParam("file") MultipartFile templateZip,
      @RequestParam("id") String dataSetId, @RequestParam("version") String version)
      throws IOException, TemplateException, TemplateIncompleteException {

    // Handles no empty latex templates
    if (!templateZip.isEmpty()) {
      Path zipTmpFilePath = Files.createTempFile(dataSetId.replace("!", ""), ".zip");
      File zipTmpFile = zipTmpFilePath.toFile();
      templateZip.transferTo(zipTmpFile);
      zipTmpFile.setWritable(true);
      Task task = taskService.createTask(Task.TaskType.DATA_SET_REPORT);
      URI pollUri = URI.create("/api/tasks/" + task.getId());
      // fill the data with data and store the template into mongodb / gridfs
      dataSetReportService.generateReport(zipTmpFilePath, templateZip.getOriginalFilename(),
          dataSetId, task, version);
      return ResponseEntity.accepted().location(pollUri).body(task);
    } else {
      // Return bad request, if file is empty.
      return ResponseEntity.badRequest().body(null);
    }

  }
}

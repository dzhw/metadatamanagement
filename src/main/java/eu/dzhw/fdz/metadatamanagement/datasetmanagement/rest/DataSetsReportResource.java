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
import eu.dzhw.fdz.metadatamanagement.common.service.CounterService;
import eu.dzhw.fdz.metadatamanagement.common.service.TaskService;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.exception.TemplateIncompleteException;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.service.DataSetReportService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;

/**
 * This Resource handles the upload of tex templates for the variable report.
 * 
 * @author Daniel Katzberg
 *
 */
@Controller
@RequestMapping("/api")
@Slf4j
public class DataSetsReportResource {

  @Autowired
  private DataSetReportService dataSetReportService;

  @Autowired
  private CounterService counterService;
  @Autowired
  TaskService taskService;


  /**
   * Accept latex templates under the given request mapping.
   * 
   * @param multiPartFile The latex template as multipart file
   * @param dataSetId the id of the data set, from where the file was uploaded
   * @throws IOException Handles io exception for the template. (Freemarker Templates)
   * @throws TemplateException Handles template exceptions. (Freemarker Templates)
   */
  @PostMapping(value = "/data-sets/report")
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public ResponseEntity<Task> uploadFile(@RequestParam("file") MultipartFile multiPartFile,
      @RequestParam("id") String dataSetId)
      throws IOException, TemplateException, TemplateIncompleteException {

    // Handles no empty latex templates
    if (!multiPartFile.isEmpty()) {
      Path zipTmpFilePath = Files.createTempFile(dataSetId.replace("!", ""), ".zip");
      File zipTmpFile = zipTmpFilePath.toFile();
      multiPartFile.transferTo(zipTmpFile);
      zipTmpFile.setWritable(true);
      
      URI pollUri;
      String taskId = Long.toString(counterService.getNextSequence(Task.class.getName()));
      pollUri = URI.create("/api/tasks/" + taskId);
      Task task = taskService.createTask(taskId);
      // fill the data with data and store the template into mongodb / gridfs
      dataSetReportService.generateReport(zipTmpFilePath, multiPartFile.getOriginalFilename(), dataSetId, task);
      return ResponseEntity.accepted().location(pollUri).body(task);
    } else {
      // Return bad request, if file is empty.
      return ResponseEntity.badRequest().body(null);
    }

  }
}

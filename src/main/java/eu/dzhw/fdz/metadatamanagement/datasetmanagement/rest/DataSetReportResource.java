package eu.dzhw.fdz.metadatamanagement.datasetmanagement.rest;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import eu.dzhw.fdz.metadatamanagement.common.domain.Task;
import eu.dzhw.fdz.metadatamanagement.common.domain.Task.TaskType;
import eu.dzhw.fdz.metadatamanagement.common.service.TaskManagementService;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.exception.TemplateIncompleteException;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.service.DataSetAttachmentService;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.service.DataSetReportService;
import eu.dzhw.fdz.metadatamanagement.mailmanagement.service.MailService;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.authmanagement.service.AuditorService;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;

/**
 * This Resource handles the filling of the tex template and the upload of the compiled file.
 */
@Controller
@RequiredArgsConstructor
public class DataSetReportResource {
  private final DataSetReportService dataSetReportService;

  private final DataSetAttachmentService dataSetAttachmentService;

  private final MailService mailService;

  private final AuditorService auditorService;

  private final TaskManagementService taskService;

  @Value("${metadatamanagement.projectmanagement.email}")
  private String sender;

  /**
   * Start the generation of a dataset report for the given {@link DataSet}.
   *
   * @param dataSetId The id of the {@link DataSet} for which we need to generate the report.
   * @param version The version of the report.
   * @param languages List of languages for which the report needs to be generated. Currently only
   *        de, en.
   * @return ok if the generation has been started successfully.
   * @throws IOException If the external task cannot be started.
   */
  @PostMapping(value = "/api/data-sets/{dataSetId}/report/generate/{version:.+}")
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public ResponseEntity<?> startReportGeneration(@PathVariable("dataSetId") String dataSetId,
      @PathVariable("version") String version, HttpServletRequest request,
      @RequestParam(name = "languages", defaultValue = "de") List<String> languages)
      throws IOException {
    taskService.startReportTasks(dataSetId, version, languages,
        request.getUserPrincipal().getName(), TaskType.DATA_SET_REPORT);
    return ResponseEntity.ok().build();
  }

  /**
   * Fill the given zip file which contains latex templates.
   *
   * @param templateZip The latex template as multipart file
   * @param dataSetId the id of the data set, for which the template needs to be processed
   * @param version The version of the dataset report as it is displayed on the first page
   * @throws IOException Handles io exception for the template. (Freemarker Templates)
   * @throws TemplateException Handles template exceptions. (Freemarker Templates)
   */
  @PostMapping(value = "/api/data-sets/{dataSetId}/report/fill-template")
  @Secured(value = {AuthoritiesConstants.TASK_USER})
  public ResponseEntity<Task> fillTemplate(@RequestParam("file") MultipartFile templateZip,
      @PathVariable("dataSetId") String dataSetId, @RequestParam("version") String version)
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

  /**
   * Upload the generated dataset report and attach it to the given {@link DataSet}.
   *
   * @param reportFile The pdf report to attach to the given {@link DataSet}
   * @param language The language of the report. Currently supports only 'de' or 'en'.
   * @param dataSetId The id of the {@link DataSet} to which this file shall be attached.
   * @param onBehalfOf Username of the MDM user who will receive an email, when the report has been
   *        successfully attached.
   * @return 200 if attaching succeeded.
   * @throws IOException Thrown if the multipart file cannot be read.
   */
  @PostMapping(value = "/api/data-sets/{dataSetId}/report/{language}")
  @Secured(value = {AuthoritiesConstants.TASK_USER})
  public ResponseEntity<?> uploadReport(@RequestParam("file") MultipartFile reportFile,
      @PathVariable("dataSetId") String dataSetId, @RequestParam("onBehalfOf") String onBehalfOf,
      @PathVariable("language") String language) throws IOException {
    try (auditorService) {
      var user = auditorService.findAndSetOnBehalfAuditor(onBehalfOf);
      dataSetAttachmentService.attachDataSetReport(dataSetId, language, reportFile);
      mailService.sendDataSetReportGeneratedMail(user, dataSetId, language, sender);
    }

    return ResponseEntity.ok().build();
  }
}

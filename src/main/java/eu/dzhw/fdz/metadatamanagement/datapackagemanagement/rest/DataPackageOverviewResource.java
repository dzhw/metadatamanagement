package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.rest;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.SecurityUtils;
import eu.dzhw.fdz.metadatamanagement.authmanagement.service.AuditorService;
import eu.dzhw.fdz.metadatamanagement.common.domain.Task;
import eu.dzhw.fdz.metadatamanagement.common.domain.Task.TaskType;
import eu.dzhw.fdz.metadatamanagement.common.service.TaskManagementService;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.service.DataPackageAttachmentService;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.service.DataPackageOverviewService;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.exception.TemplateIncompleteException;
import eu.dzhw.fdz.metadatamanagement.mailmanagement.service.MailService;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;

/**
 * This Resource handles the filling of the tex template and the upload of the compiled file for
 * creating data package overview.
 */
@Controller
@RequiredArgsConstructor
public class DataPackageOverviewResource {
  private final MailService mailService;

  private final DataPackageOverviewService dataPackageOverviewService;

  private final DataPackageAttachmentService dataPackageAttachmentService;

  private final AuditorService auditorService;

  private final TaskManagementService taskService;

  @Value("${metadatamanagement.projectmanagement.email}")
  private String sender;

  /**
   * Start the generation of a data package overview for the given {@link DataPackage}.
   *
   * @param dataPackageId The id of the {@link DataPackage} for which we need to generate the
   *        overview.
   * @param version The version of the DOI in the overview.
   * @param languages List of languages for which the report needs to be generated. Currently only
   *        de, en.
   * @return ok if the generation has been started successfully.
   * @throws IOException If the external task cannot be started.
   */
  @PostMapping(value = "/api/data-packages/{dataPackageId}/overview/generate/{version:.+}")
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public ResponseEntity<?> startOverviewGeneration(
      @PathVariable("dataPackageId") String dataPackageId, @PathVariable("version") String version,
      @RequestParam(name = "languages", defaultValue = "de") List<String> languages)
      throws IOException {
    taskService.startReportTasks(dataPackageId, version, languages,
        SecurityUtils.getCurrentUserLogin(), TaskType.DATA_PACKAGE_OVERVIEW);
    return ResponseEntity.ok().build();
  }

  /**
   * Fill the given zip file which contains latex templates.
   *
   * @param templateZip The latex template as multipart file
   * @param dataPackageId the id of the data package, for which the template needs to be processed
   * @param version The version of the DOI of the data package
   * @throws IOException Handles io exception for the template. (Freemarker Templates)
   * @throws TemplateException Handles template exceptions. (Freemarker Templates)
   */
  @PostMapping(value = "/api/data-packages/{dataPackageId}/overview/fill-template")
  @Secured(value = {AuthoritiesConstants.TASK_USER})
  public ResponseEntity<Task> fillTemplate(@RequestParam("file") MultipartFile templateZip,
      @PathVariable("dataPackageId") String dataPackageId, @RequestParam("version") String version)
      throws IOException, TemplateException, TemplateIncompleteException {

    // Handles no empty latex templates
    if (!templateZip.isEmpty()) {
      Path zipTmpFilePath = Files.createTempFile(dataPackageId.replace("!", ""), ".zip");
      File zipTmpFile = zipTmpFilePath.toFile();
      templateZip.transferTo(zipTmpFile);
      zipTmpFile.setWritable(true);
      Task task = taskService.createTask(Task.TaskType.DATA_PACKAGE_OVERVIEW);
      URI pollUri = URI.create("/api/tasks/" + task.getId());
      // fill the data with data and store the template into mongodb / gridfs
      dataPackageOverviewService.generateReport(zipTmpFilePath, templateZip.getOriginalFilename(),
          dataPackageId, task, version);
      return ResponseEntity.accepted().location(pollUri).body(task);
    } else {
      // Return bad request, if file is empty.
      return ResponseEntity.badRequest().body(null);
    }
  }

  /**
   * Upload the generated data package overview and attach it to the given {@link DataPackage}.
   *
   * @param overviewFile The pdf overview to attach to the given {@link DataPackage}
   * @param language The language of the overview. Currently supports only 'de' or 'en'.
   * @param dataPackageId The id of the {@link DataPackage} to which this file shall be attached.
   * @param onBehalfOf Username of the MDM user who will receive an email, when the report has been
   *        successfully attached.
   * @return 200 if attaching succeeded.
   * @throws IOException Thrown if the multipart file cannot be read.
   */
  @PostMapping(value = "/api/data-packages/{dataPackageId}/overview/{language}")
  @Secured(value = {AuthoritiesConstants.TASK_USER})
  public ResponseEntity<?> uploadOverview(@RequestParam("file") MultipartFile overviewFile,
      @PathVariable("dataPackageId") String dataPackageId,
      @RequestParam("onBehalfOf") String onBehalfOf, @PathVariable("language") String language)
      throws IOException {
    try (auditorService) {
      var user = auditorService.findAndSetOnBehalfAuditor(onBehalfOf);
      dataPackageAttachmentService.attachDataPackageOverview(dataPackageId, language, overviewFile);
      mailService.sendDataPackageOverviewGeneratedMail(user, dataPackageId, language, sender);
    }

    return ResponseEntity.ok().build();
  }
}

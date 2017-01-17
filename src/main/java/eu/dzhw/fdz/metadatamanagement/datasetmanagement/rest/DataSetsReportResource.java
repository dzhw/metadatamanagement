package eu.dzhw.fdz.metadatamanagement.datasetmanagement.rest;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.codahale.metrics.annotation.Timed;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.exception.TemplateIncompleteException;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.service.DataSetReportService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import freemarker.template.TemplateException;

/**
 * This Resource handles the upload of tex templates for the variable report.
 * 
 * @author Daniel Katzberg
 *
 */
@Controller
@RequestMapping("/api")
public class DataSetsReportResource {

  @Autowired
  private DataSetReportService dataSetReportService;

  /**
   * Accept latex templates under the given request mapping.
   * 
   * @param multiPartFile The latex template as multipart file
   * @param id the id of the data acquision project, from where the file was uploaded
   * @throws IOException Handles io exception for the template. (Freemarker Templates)
   * @throws TemplateException Handles template exceptions. (Freemarker Templates)
   */
  @RequestMapping(value = "/data-sets/report")
  @Timed
  @Secured(AuthoritiesConstants.PUBLISHER)
  public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile multiPartFile,
      @RequestParam("id") String id) throws IOException, TemplateException, 
      TemplateIncompleteException {

    // Handles no empty latex templates
    if (!multiPartFile.isEmpty()) {

      // fill the data with data and store the template into mongodb / gridfs
      String fileName = this.dataSetReportService.generateReport(multiPartFile, id);
      
      if (fileName == null) {
        // Return bad request, if tex template is incomplete
        //The cration of the error json object happens in the ExceptionTranslationHandler
        return ResponseEntity
          .badRequest()
          .body(null);
      }
      

      // Return ok. Status 200.
      return ResponseEntity.ok()
        .contentLength(fileName.length())
        .contentType(MediaType.TEXT_PLAIN)
        .body(fileName);

    } else {
      // Return bad request, if file is empty.
      return ResponseEntity.badRequest()
        .body(null);
    }
  }
}

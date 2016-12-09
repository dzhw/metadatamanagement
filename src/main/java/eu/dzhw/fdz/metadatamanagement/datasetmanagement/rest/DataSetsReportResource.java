package eu.dzhw.fdz.metadatamanagement.datasetmanagement.rest;

import java.io.IOException;

import javax.inject.Inject;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.service.DataSetReportService;
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

  @Inject
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
  public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile multiPartFile,
      @RequestParam("id") String id) throws IOException, TemplateException {

    // Handles no empty latex templates
    if (!multiPartFile.isEmpty()) {

      // fill the data with data and store the template into mongodb / gridfs
      String fileName = this.dataSetReportService.generateReport(multiPartFile, id);

      
//      if (fileName == null) {
//        return ResponseEntity
//            .status(HttpStatus.NOT_EXTENDED)
//            .contentType(MediaType.TEXT_PLAIN)
//            .body(this.dataSetReportService.getMissingFiles());
//      }

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

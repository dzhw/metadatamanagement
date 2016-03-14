package eu.dzhw.fdz.metadatamanagement.web.rest;

import java.io.IOException;

import javax.inject.Inject;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import eu.dzhw.fdz.metadatamanagement.service.reporter.LatexTemplateService;
import freemarker.template.TemplateException;

/**
 * This Resource handles the upload of tex templates for the variable report.
 * 
 * @author Daniel Katzberg
 *
 */
@Controller
@RequestMapping("/api")
public class VariableReportResource {

  @Inject
  private LatexTemplateService latexDataFillService;

  /**
   * Accept latex templates under the given request mapping.
   * 
   * @param file The latex template as multipart file
   * @param id the id of the data acquision project, from where the file was uploaded
   * @throws IOException Handles io exception for the template. (Freemarker Templates)
   * @throws TemplateException Handles template exceptions. (Freemarker Templates)
   */
  @RequestMapping(value = "/dataSets/report")
  public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
      @RequestParam("id") String id) throws IOException, TemplateException {

    // Handles no empty latex templates
    if (!file.isEmpty()) {

      // get File as bytes
      byte[] fileAsBytes = file.getBytes();

      // fill the data with data and store the template into mongodb / gridfs
      String fileName =
          this.latexDataFillService.generateReport(new String(fileAsBytes, "UTF-8"), id);

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

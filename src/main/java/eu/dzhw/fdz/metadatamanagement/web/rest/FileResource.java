package eu.dzhw.fdz.metadatamanagement.web.rest;

import java.io.IOException;

import javax.inject.Inject;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import eu.dzhw.fdz.metadatamanagement.service.reporter.LatexDataFillService;
import freemarker.template.TemplateException;

/**
 * REST controller for uploading a file into the mongo db.
 * 
 * @author Daniel Katzberg
 *
 */
@Controller
@RequestMapping("/api")
public class FileResource {

  @Inject
  private LatexDataFillService latexDataFillService;

  /**
   * Accept latex templates under the given request mapping.
   * 
   * @param file The latex template as multipart file
   * @param id the id of the data acquision project, from where the file was uploaded
   * @throws IOException Handles io exception for the template.
   */
  @RequestMapping(value = "/files/upload/tex")
  public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
      @RequestParam("id") String id) throws IOException {

    // Handles no empty latex templates
    if (!file.isEmpty()) {
      byte[] bytes;
      bytes = file.getBytes();

      // fill the data with data and store the template into mongodb / gridfs
      try {
        this.latexDataFillService.fillLatexTemplateWithData(new String(bytes, "UTF-8"), id);
      } catch (TemplateException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

    // Return ok. Status 200.
    return ResponseEntity.ok()
      .build();
  }

}

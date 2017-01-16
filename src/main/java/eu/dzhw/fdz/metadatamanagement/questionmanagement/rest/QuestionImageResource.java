package eu.dzhw.fdz.metadatamanagement.questionmanagement.rest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.codahale.metrics.annotation.Timed;

import eu.dzhw.fdz.metadatamanagement.questionmanagement.service.QuestionImageService;

/**
 * REST controller for uploading an image.
 */
@Controller
@RequestMapping("/api")
public class QuestionImageResource {

  @Autowired
  private QuestionImageService imageService;
  
  /**
   * REST method for for uploading an image.
   * @param multiPartFile the image
   * @param id id of image
   * @return response
   * @throws IOException write Exception 
   * @throws URISyntaxException if the file has an invalid URI
   */
  @RequestMapping(path = "/questions/images", method = RequestMethod.POST)
  @Timed
  public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile multiPartFile,
      @RequestParam("id") String id) throws IOException, URISyntaxException {
    if (!multiPartFile.isEmpty()) {
      String gridFsFileName = imageService.saveQuestionImage(multiPartFile.getInputStream(), 
          id, multiPartFile.getContentType());
      return ResponseEntity.created(new URI("/public/files" + gridFsFileName))
        .body(null);
    } else {
      return ResponseEntity.badRequest()
        .body(null);
    }
  }
}

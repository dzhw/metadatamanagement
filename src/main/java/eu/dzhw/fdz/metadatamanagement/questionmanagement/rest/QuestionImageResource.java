package eu.dzhw.fdz.metadatamanagement.questionmanagement.rest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.codahale.metrics.annotation.Timed;

import eu.dzhw.fdz.metadatamanagement.questionmanagement.service.QuestionImageService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

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
   * @param questionId questionId of image
   * @return response
   * @throws IOException write Exception 
   * @throws URISyntaxException if the file has an invalid URI
   */
  @RequestMapping(path = "/questions/images", method = RequestMethod.POST)
  @Timed
  @Secured(AuthoritiesConstants.PUBLISHER)
  public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile multiPartFile,
      @RequestParam("questionId") String questionId) throws IOException, URISyntaxException {
    if (!multiPartFile.isEmpty()) {
      String gridFsFileName = imageService.saveQuestionImage(multiPartFile, 
          questionId);
      return ResponseEntity.created(new URI("/public/files" + gridFsFileName))
        .body(null);
    } else {
      return ResponseEntity.badRequest()
        .body(null);
    }
  }
  
  /**
   * Delete all images of the given question.
   * 
   * @param questionId The id of a question.
   */
  @RequestMapping(path = "/questions/{questionId}/images", method = RequestMethod.DELETE)
  @Timed
  @Secured(AuthoritiesConstants.PUBLISHER)
  public ResponseEntity<?> deleteAllByQuestionId(@PathVariable("questionId") String questionId) {
    if (!StringUtils.isEmpty(questionId)) {
      imageService.deleteQuestionImage(questionId);
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.badRequest()
        .body(null);
    }
  }
}

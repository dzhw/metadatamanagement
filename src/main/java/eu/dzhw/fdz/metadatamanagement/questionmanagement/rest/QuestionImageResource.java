package eu.dzhw.fdz.metadatamanagement.questionmanagement.rest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.QuestionImageMetadata;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.service.QuestionImageService;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for uploading an image.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class QuestionImageResource {

  private final QuestionImageService imageService;

  /**
   * REST method for for uploading images to a question with metadata.
   *
   * @param multiPartFile the image
   * @param questionImageMetadata the metadata of the image
   * @return response
   * @throws IOException write Exception
   * @throws URISyntaxException if the file has an invalid URI
   */
  @RequestMapping(path = "/questions/images", method = RequestMethod.POST)
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile multiPartFile,
      @RequestPart("questionImageMetadata") @Valid QuestionImageMetadata questionImageMetadata)
      throws IOException, URISyntaxException {
    if (!multiPartFile.isEmpty()) {
      String gridFsFileName = imageService.saveQuestionImage(multiPartFile, questionImageMetadata);
      return ResponseEntity
          .created(new URI("/public/files" + URLEncoder.encode(gridFsFileName, "UTF-8")))
          .body(null);
    } else {
      return ResponseEntity.badRequest().body(null);
    }
  }

  /**
   * Load all images with metadata objects for the given question id.
   *
   * @param questionId The id of an question.
   * @return A list of image metadata objects.
   */
  @RequestMapping(path = "/questions/{questionId}/images", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> findByQuestionId(@PathVariable("questionId") String questionId) {
    List<QuestionImageMetadata> metadata = imageService.findByQuestionId(questionId);
    return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(metadata);
  }

  /**
   * Delete all images of the given question.
   *
   * @param questionId The id of a question.
   */
  @RequestMapping(path = "/questions/{questionId}/images", method = RequestMethod.DELETE)
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public ResponseEntity<?> deleteAllByQuestionId(@PathVariable("questionId") String questionId) {
    imageService.deleteQuestionImages(questionId);
    return ResponseEntity.noContent().build();
  }
}

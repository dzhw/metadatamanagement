package eu.dzhw.fdz.metadatamanagement.surveymanagement.rest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.SurveyResponseRateImageMetadata;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.service.SurveyResponseRateImageService;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for uploading an survey image.
 *
 * @author Daniel Katzberg
 */
@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class SurveyResponseRateImageResource {

  private final SurveyResponseRateImageService surveyResponseRateImageService;

  /**
   * REST method for for uploading an image.
   *
   * @param multiPartFile the image
   * @param surveyResponseRateImageMetadata the metadata for the image
   * @return response
   * @throws IOException write Exception
   * @throws URISyntaxException write uri exception
   */
  @RequestMapping(path = "/surveys/images", method = RequestMethod.POST)
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile multiPartFile,
      @RequestPart("surveyResponseRateImageMetadata")
      @Valid SurveyResponseRateImageMetadata surveyResponseRateImageMetadata)
      throws IOException, URISyntaxException {
    if (!multiPartFile.isEmpty() && this.surveyResponseRateImageService.checkResponseRateFileName(
        surveyResponseRateImageMetadata.getSurveyId(), multiPartFile.getOriginalFilename())) {
      // We need the original file, because we do not know if it is a german or english file.
      // We just know it is a valid name.
      String imageName = this.surveyResponseRateImageService.saveSurveyImage(multiPartFile,
          surveyResponseRateImageMetadata, multiPartFile.getOriginalFilename());
      return ResponseEntity.created(new URI("/public/files" + imageName))
          .contentLength(imageName.length()).contentType(MediaType.TEXT_PLAIN).body(imageName);
    } else {
      return ResponseEntity.badRequest().body(null);
    }
  }

  /**
   * Delete all images of the given survey.
   *
   * @param surveyId The id of an survey.
   */
  @RequestMapping(path = "/surveys/{surveyId}/images", method = RequestMethod.DELETE)
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public ResponseEntity<?> deleteAllBySurveyId(@PathVariable("surveyId") String surveyId) {
    surveyResponseRateImageService.deleteAllSurveyImagesById(surveyId);
    return ResponseEntity.noContent().build();
  }

  /**
   * Delete the given image of the given survey.
   *
   * @param surveyId The id of the survey.
   * @param filename The name of the image file
   * @throws IOException if gridfs access fails
   */
  @RequestMapping(path = "/surveys/{surveyId}/images/{filename:.+}", method = RequestMethod.DELETE)
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public ResponseEntity<?> deleteImage(@PathVariable("surveyId") String surveyId,
      @PathVariable("filename") String filename) throws IOException {
    surveyResponseRateImageService.deleteSurveyImage(surveyId, filename);
    return ResponseEntity.noContent().build();
  }
}

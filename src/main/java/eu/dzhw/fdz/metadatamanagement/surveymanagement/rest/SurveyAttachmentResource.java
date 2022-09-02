package eu.dzhw.fdz.metadatamanagement.surveymanagement.rest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.SurveyAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.service.SurveyAttachmentService;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for uploading an survey attachment.
 */
@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class SurveyAttachmentResource {

  private final SurveyAttachmentService surveyAttachmentService;

  /**
   * REST method for for uploading an survey attachment.
   *
   * @param multiPartFile the attachment
   * @param surveyAttachmentMetadata the metadata for the attachment
   * @return response 201 if the attachment was created
   * @throws URISyntaxException if a URI is syntactically wrong
   * @throws IOException If the attachment cannot be read
   */
  @RequestMapping(path = "/surveys/attachments", method = RequestMethod.POST)
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public ResponseEntity<String> uploadAttachment(@RequestPart("file") MultipartFile multiPartFile,
      @RequestPart("surveyAttachmentMetadata")
      @Valid SurveyAttachmentMetadata surveyAttachmentMetadata)
      throws URISyntaxException, IOException {
    surveyAttachmentService.createSurveyAttachment(multiPartFile, surveyAttachmentMetadata);
    return ResponseEntity
        .created(new URI(UriUtils.encodePath(surveyAttachmentMetadata.getId(), "UTF-8")))
        .body(null);
  }

  /**
   * Load all attachment metadata objects for the given survey id.
   *
   * @param surveyId The id of an survey.
   * @return A list of metadata objects.
   */
  @RequestMapping(path = "/surveys/{surveyId}/attachments", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> findBySurveyId(@PathVariable("surveyId") String surveyId) {
    List<SurveyAttachmentMetadata> metadata = surveyAttachmentService.findAllBySurvey(surveyId);
    return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(metadata);
  }

  /**
   * Delete all attachments of the given survey.
   *
   * @param surveyId The id of an survey.
   */
  @RequestMapping(path = "/surveys/{surveyId}/attachments", method = RequestMethod.DELETE)
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public ResponseEntity<?> deleteAllBySurveyId(@PathVariable("surveyId") String surveyId) {
    surveyAttachmentService.deleteAllBySurveyId(surveyId);
    return ResponseEntity.noContent().build();

  }

  /**
   * Delete the given attachment of the given survey.
   *
   * @param surveyId The id of the survey.
   *
   */
  @RequestMapping(path = "/surveys/{surveyId}/attachments/{filename:.+}",
      method = RequestMethod.DELETE)
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public ResponseEntity<?> delete(@PathVariable("surveyId") String surveyId,
      @PathVariable("filename") String filename) {
    surveyAttachmentService.deleteBySurveyIdAndFilename(surveyId, filename);
    return ResponseEntity.noContent().build();
  }

  /**
   * Update the metadata of the given attachment of the given survey.
   */
  @RequestMapping(path = "/surveys/{surveyId}/attachments/{filename:.+}",
      method = RequestMethod.PUT)
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public ResponseEntity<?> update(
      @Valid @RequestBody SurveyAttachmentMetadata surveyAttachmentMetadata) {
    surveyAttachmentService.updateAttachmentMetadata(surveyAttachmentMetadata);
    return ResponseEntity.noContent().build();
  }
}

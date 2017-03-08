package eu.dzhw.fdz.metadatamanagement.surveymanagement.rest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.codahale.metrics.annotation.Timed;

import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.SurveyAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.service.SurveyAttachmentService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

/**
 * REST controller for uploading an survey attachment.
 */
@Controller
@RequestMapping("/api")
public class SurveyAttachmentResource {

  @Autowired
  private SurveyAttachmentService surveyAttachmentService;

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
  @Timed
  @Secured(AuthoritiesConstants.PUBLISHER)
  public ResponseEntity<String> uploadAttachment(@RequestPart("file") MultipartFile multiPartFile,
      @RequestPart("surveyAttachmentMetadata") 
      @Valid SurveyAttachmentMetadata surveyAttachmentMetadata)
      throws URISyntaxException, IOException {
    if (!multiPartFile.isEmpty()) {
      surveyAttachmentService.createSurveyAttachment(multiPartFile.getInputStream(),
          multiPartFile.getContentType(), surveyAttachmentMetadata);
      return ResponseEntity.created(new URI(surveyAttachmentMetadata.getId()))
        .body(null);
    } else {
      return ResponseEntity.badRequest()
        .body(null);
    }
  }

  /**
   * Load all attachment metadata objects for the given survey id.
   * 
   * @param surveyId The id of an survey.
   * @return A list of metadata objects.
   */
  @RequestMapping(path = "/surveys/{surveyId}/attachments", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<?> findBySurveyId(@PathVariable("surveyId") String surveyId) {
    if (!StringUtils.isEmpty(surveyId)) {
      List<SurveyAttachmentMetadata> metadata =
          surveyAttachmentService.findAllBySurvey(surveyId);
      return ResponseEntity.ok()
          .body(metadata);
    } else {
      return ResponseEntity.badRequest()
        .body(null);
    }
  }
}

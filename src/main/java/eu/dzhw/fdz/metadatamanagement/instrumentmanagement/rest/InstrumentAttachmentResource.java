package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.rest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.codahale.metrics.annotation.Timed;

import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.InstrumentAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.service.InstrumentAttachmentService;

/**
 * REST controller for uploading an instrument attachment.
 */
@Controller
@RequestMapping("/api")
public class InstrumentAttachmentResource {

  @Inject
  private InstrumentAttachmentService instrumentAttachmentService;

  /**
   * REST method for for uploading an instrument attachment.
   * 
   * @param multiPartFile the attachment
   * @param instrumentAttachmentMetadata the metadata for the attachment
   * @return response 201 if the attachment was created
   * @throws URISyntaxException if a URI is syntactically wrong
   * @throws IOException If the attachment cannot be read
   */
  @RequestMapping(path = "/instruments/attachments", method = RequestMethod.POST)
  @Timed
  public ResponseEntity<String> uploadAttachment(@RequestPart("file") MultipartFile multiPartFile,
      @RequestPart("instrumentAttachmentMetadata") 
      @Valid InstrumentAttachmentMetadata instrumentAttachmentMetadata)
      throws URISyntaxException, IOException {
    if (!multiPartFile.isEmpty()) {
      instrumentAttachmentService.createInstrumentAttachment(multiPartFile.getInputStream(),
          multiPartFile.getContentType(), instrumentAttachmentMetadata);
      return ResponseEntity.created(new URI(instrumentAttachmentMetadata.getId()))
        .body(null);
    } else {
      return ResponseEntity.badRequest()
        .body(null);
    }
  }

  /**
   * Load all attachment metadata objects for the given instrument id.
   * 
   * @param instrumentId The id of an instrument.
   * @return A list of metadata objects.
   */
  @RequestMapping(path = "/instruments/{instrumentId}/attachments", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<?> findByInstrumentId(@PathVariable("instrumentId") String instrumentId) {
    if (!StringUtils.isEmpty(instrumentId)) {
      List<InstrumentAttachmentMetadata> metadata =
          instrumentAttachmentService.findAllByInstrument(instrumentId);
      if (metadata.isEmpty()) {
        return ResponseEntity.notFound()
          .build();
      } else {
        return ResponseEntity.ok()
          .body(metadata);
      }
    } else {
      return ResponseEntity.badRequest()
        .body(null);
    }
  }
}

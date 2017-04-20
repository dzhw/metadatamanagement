package eu.dzhw.fdz.metadatamanagement.datasetmanagement.rest;

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

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSetAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.service.DataSetAttachmentService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

/**
 * REST controller for uploading an data sets attachment.
 */
@Controller
@RequestMapping("/api")
public class DataSetAttachmentResource {

  @Autowired
  private DataSetAttachmentService dataSetAttachmentService;

  /**
   * REST method for for uploading an data sets attachment.
   * 
   * @param multiPartFile the attachment
   * @param dataSetAttachmentMetadata the metadata for the attachment
   * @return response 201 if the attachment was created
   * @throws URISyntaxException if a URI is syntactically wrong
   * @throws IOException If the attachment cannot be read
   */ 
  @RequestMapping(path = "/data-sets/attachments", method = RequestMethod.POST)
  @Timed
  @Secured(AuthoritiesConstants.PUBLISHER)
  public ResponseEntity<String> uploadAttachment(@RequestPart("file") MultipartFile multiPartFile,
      @RequestPart("dataSetAttachmentMetadata") 
      @Valid DataSetAttachmentMetadata dataSetAttachmentMetadata)
      throws URISyntaxException, IOException {
    dataSetAttachmentService.createDataSetAttachment(multiPartFile.getInputStream(),
        multiPartFile.getContentType(), dataSetAttachmentMetadata);
    return ResponseEntity.created(new URI(dataSetAttachmentMetadata.getId()))
        .body(null);
  }

  /**
   * Load all attachment metadata objects for the given data set id.
   * 
   * @param dataSetId The id of an data set.
   * @return A list of metadata objects.
   */
  @RequestMapping(path = "/data-sets/{dataSetId}/attachments", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<?> findByDataSetId(@PathVariable("dataSetId") String dataSetId) {
    if (!StringUtils.isEmpty(dataSetId)) {
      List<DataSetAttachmentMetadata> metadata =
          dataSetAttachmentService.findAllByDataSet(dataSetId);
      return ResponseEntity.ok()
          .body(metadata);
    } else {
      return ResponseEntity.badRequest()
        .body(null);
    }
  }
  
  /**
   * Delete all attachments of the given data set.
   * 
   * @param dataSetId The id of an data set.
   */
  @RequestMapping(path = "/data-sets/{dataSetId}/attachments", method = RequestMethod.DELETE)
  @Timed
  @Secured(AuthoritiesConstants.PUBLISHER)
  public ResponseEntity<?> deleteAllByDataSetId(@PathVariable("dataSetId") String dataSetId) {
    if (!StringUtils.isEmpty(dataSetId)) {
      dataSetAttachmentService.deleteAllByDataSetId(dataSetId);
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.badRequest()
        .body(null);
    }
  }
}

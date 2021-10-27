package eu.dzhw.fdz.metadatamanagement.datasetmanagement.rest;

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

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSetAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.service.DataSetAttachmentService;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for uploading an data sets attachment.
 */
@Controller
@RequiredArgsConstructor
public class DataSetAttachmentResource {

  private final DataSetAttachmentService dataSetAttachmentService;

  /**
   * REST method for for uploading an dataSet attachment.
   *
   * @param multiPartFile the attachment
   * @param dataSetAttachmentMetadata the metadata for the attachment
   * @return response 201 if the attachment was created
   * @throws URISyntaxException if a URI is syntactically wrong
   * @throws IOException If the attachment cannot be read
   */
  @RequestMapping(path = "/api/data-sets/attachments", method = RequestMethod.POST)
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public ResponseEntity<String> uploadAttachment(@RequestPart("file") MultipartFile multiPartFile,
      @RequestPart("dataSetAttachmentMetadata")
      @Valid DataSetAttachmentMetadata dataSetAttachmentMetadata)
      throws URISyntaxException, IOException {
    dataSetAttachmentService.createDataSetAttachment(multiPartFile,
        dataSetAttachmentMetadata);
    return ResponseEntity.created(new URI(UriUtils.encodePath(
        dataSetAttachmentMetadata.getId(), "UTF-8")))
        .body(null);
  }

  /**
   * Load all attachment metadata objects for the given dataSet id.
   *
   * @param dataSetId The id of an dataSet.
   * @return A list of metadata objects.
   */
  @RequestMapping(path = "/api/data-sets/{dataSetId}/attachments", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> findByDataSetId(@PathVariable("dataSetId") String dataSetId) {
    List<DataSetAttachmentMetadata> metadata = dataSetAttachmentService.findAllByDataSet(dataSetId);
    return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(metadata);
  }

  /**
   * Delete all attachments of the given dataSet.
   *
   * @param dataSetId The id of the dataSet.
   */
  @RequestMapping(path = "/api/data-sets/{dataSetId}/attachments", method = RequestMethod.DELETE)
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public ResponseEntity<?> deleteAllByDataSetId(@PathVariable("dataSetId") String dataSetId) {
    dataSetAttachmentService.deleteAllByDataSetId(dataSetId);
    return ResponseEntity.noContent().build();
  }

  /**
   * Delete the given attachments of the given dataSet.
   *
   * @param dataSetId The id of the dataSet.
   *
   */
  @RequestMapping(path = "/api/data-sets/{dataSetId}/attachments/{filename:.+}",
      method = RequestMethod.DELETE)
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public ResponseEntity<?> delete(@PathVariable("dataSetId") String dataSetId,
      @PathVariable("filename") String filename) {
    dataSetAttachmentService.deleteByDataSetIdAndFilename(dataSetId, filename);
    return ResponseEntity.noContent().build();
  }

  /**
   * Update the metadata of the given attachment of the given dataSet.
   */
  @RequestMapping(path = "/api/data-sets/{dataSetId}/attachments/{filename:.+}",
      method = RequestMethod.PUT)
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public ResponseEntity<?> update(
      @Valid @RequestBody DataSetAttachmentMetadata dataSetAttachmentMetadata) {
    dataSetAttachmentService.updateAttachmentMetadata(dataSetAttachmentMetadata);
    return ResponseEntity.noContent().build();
  }
}

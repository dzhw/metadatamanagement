package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.rest;

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

import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackageAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.service.DataPackageAttachmentService;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for uploading an dataPackage attachment.
 */
@Controller
@RequiredArgsConstructor
public class DataPackageAttachmentResource {

  private final DataPackageAttachmentService dataPackageAttachmentService;

  /**
   * REST method for for uploading an dataPackage attachment.
   *
   * @param multiPartFile the attachment
   * @param dataPackageAttachmentMetadata the metadata for the attachment
   * @return response 201 if the attachment was created
   * @throws URISyntaxException if a URI is syntactically wrong
   * @throws IOException If the attachment cannot be read
   */
  @RequestMapping(path = "/api/data-packages/attachments", method = RequestMethod.POST)
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public ResponseEntity<String> uploadAttachment(@RequestPart("file") MultipartFile multiPartFile,
      @RequestPart("dataPackageAttachmentMetadata")
      @Valid DataPackageAttachmentMetadata dataPackageAttachmentMetadata)
      throws URISyntaxException, IOException {
    dataPackageAttachmentService.createDataPackageAttachment(multiPartFile,
        dataPackageAttachmentMetadata);
    return ResponseEntity
        .created(new URI(UriUtils.encodePath(dataPackageAttachmentMetadata.getId(), "UTF-8")))
        .body(null);
  }

  /**
   * Load all attachment metadata objects for the given dataPackage id.
   *
   * @param dataPackageId The id of an dataPackage.
   * @return A list of metadata objects.
   */
  @RequestMapping(path = "/api/data-packages/{dataPackageId}/attachments",
      method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> findByDataPackageId(
      @PathVariable("dataPackageId") String dataPackageId) {
    List<DataPackageAttachmentMetadata> metadata =
        dataPackageAttachmentService.findAllByDataPackage(dataPackageId);
    return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(metadata);
  }

  /**
   * Delete all attachments of the given dataPackage.
   *
   * @param dataPackageId The id of the dataPackage.
   */
  @RequestMapping(path = "/api/data-packages/{dataPackageId}/attachments",
      method = RequestMethod.DELETE)
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public ResponseEntity<?> deleteAllByDataPackageId(
      @PathVariable("dataPackageId") String dataPackageId) {
    dataPackageAttachmentService.deleteAllByDataPackageId(dataPackageId);
    return ResponseEntity.noContent().build();
  }

  /**
   * Delete the given attachments of the given dataPackage.
   *
   * @param dataPackageId The id of the dataPackage.
   *
   */
  @RequestMapping(path = "/api/data-packages/{dataPackageId}/attachments/{filename:.+}",
      method = RequestMethod.DELETE)
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public ResponseEntity<?> delete(@PathVariable("dataPackageId") String dataPackageId,
      @PathVariable("filename") String filename) {
    dataPackageAttachmentService.deleteByDataPackageIdAndFilename(dataPackageId, filename);
    return ResponseEntity.noContent().build();
  }

  /**
   * Update the metadata of the given attachment of the given dataPackage.
   */
  @RequestMapping(path = "/api/data-packages/{dataPackageId}/attachments/{filename:.+}",
      method = RequestMethod.PUT)
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public ResponseEntity<?> update(
      @Valid @RequestBody DataPackageAttachmentMetadata dataPackageAttachmentMetadata) {
    dataPackageAttachmentService.updateAttachmentMetadata(dataPackageAttachmentMetadata);
    return ResponseEntity.noContent().build();
  }
}

package eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.rest;

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

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.AnalysisPackage;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.AnalysisPackageAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.service.AnalysisPackageAttachmentService;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for uploading an analysis package attachment.
 */
@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class AnalysisPackageAttachmentResource {

  private final AnalysisPackageAttachmentService analysisPackageAttachmentService;

  /**
   * REST method for for uploading an analysis package attachment.
   *
   * @param multiPartFile the attachment
   * @param analysisPackageAttachmentMetadata the metadata for the attachment
   * @return response 201 if the attachment was created
   * @throws URISyntaxException if a URI is syntactically wrong
   * @throws IOException If the attachment cannot be read
   */
  @RequestMapping(path = "/analysis-packages/attachments", method = RequestMethod.POST)
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public ResponseEntity<String> uploadAttachment(@RequestPart("file") MultipartFile multiPartFile,
      @RequestPart("analysisPackageAttachmentMetadata")
      @Valid AnalysisPackageAttachmentMetadata analysisPackageAttachmentMetadata)
      throws URISyntaxException, IOException {
    analysisPackageAttachmentService.createAnalysisPackageAttachment(multiPartFile,
        analysisPackageAttachmentMetadata);
    return ResponseEntity
        .created(new URI(UriUtils.encodePath(analysisPackageAttachmentMetadata.getId(), "UTF-8")))
        .body(null);
  }

  /**
   * Load all attachment metadata objects for the given analysis package id.
   *
   * @param analysisPackageId The id of an {@link AnalysisPackage}.
   * @return A list of metadata objects.
   */
  @RequestMapping(path = "/analysis-packages/{analysisPackageId}/attachments",
      method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> findByAnalysisPackageId(
      @PathVariable("analysisPackageId") String analysisPackageId) {
    List<AnalysisPackageAttachmentMetadata> metadata =
        analysisPackageAttachmentService.findAllByAnalysisPackage(analysisPackageId);
    return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(metadata);
  }

  /**
   * Delete all attachments of the given {@link AnalysisPackage}.
   *
   * @param analysisPackageId The id of the {@link AnalysisPackage}.
   */
  @RequestMapping(path = "/analysis-packages/{analysisPackageId}/attachments",
      method = RequestMethod.DELETE)
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public ResponseEntity<?> deleteAllByAnalysisPackageId(
      @PathVariable("analysisPackageId") String analysisPackageId) {
    analysisPackageAttachmentService.deleteAllByAnalysisPackageId(analysisPackageId);
    return ResponseEntity.noContent().build();
  }

  /**
   * Delete the given attachments of the given {@link AnalysisPackage}.
   *
   * @param analysisPackageId The id of the {@link AnalysisPackage}.
   *
   */
  @RequestMapping(path = "/analysis-packages/{analysisPackageId}/attachments/{filename:.+}",
      method = RequestMethod.DELETE)
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public ResponseEntity<?> delete(@PathVariable("analysisPackageId") String analysisPackageId,
      @PathVariable("filename") String filename) {
    analysisPackageAttachmentService.deleteByAnalysisPackageIdAndFilename(analysisPackageId,
        filename);
    return ResponseEntity.noContent().build();
  }

  /**
   * Update the metadata of the given attachment of the given analysisPackage.
   */
  @RequestMapping(path = "/analysis-packages/{analysisPackageId}/attachments/{filename:.+}",
      method = RequestMethod.PUT)
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public ResponseEntity<?> update(
      @Valid @RequestBody AnalysisPackageAttachmentMetadata analysisPackageAttachmentMetadata) {
    analysisPackageAttachmentService.updateAttachmentMetadata(analysisPackageAttachmentMetadata);
    return ResponseEntity.noContent().build();
  }
}

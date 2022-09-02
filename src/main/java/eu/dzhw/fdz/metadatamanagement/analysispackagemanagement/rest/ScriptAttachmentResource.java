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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.AnalysisPackage;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.Script;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.ScriptAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.service.ScriptAttachmentService;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for uploading a script attachments.
 */
@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class ScriptAttachmentResource {

  private final ScriptAttachmentService scriptAttachmentService;

  /**
   * REST method for for uploading an script attachment.
   *
   * @param multiPartFile the attachment
   * @param scriptAttachmentMetadata the metadata for the attachment
   * @return response 201 if the attachment was created
   * @throws URISyntaxException if a URI is syntactically wrong
   * @throws IOException If the attachment cannot be read
   */
  @RequestMapping(path = "/analysis-packages/{analysisPackageId}/scripts/attachments",
      method = RequestMethod.POST)
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public ResponseEntity<String> uploadAttachment(@RequestPart("file") MultipartFile multiPartFile,
      @RequestPart("scriptAttachmentMetadata")
      @Valid ScriptAttachmentMetadata scriptAttachmentMetadata)
      throws URISyntaxException, IOException {
    scriptAttachmentService.createScriptAttachment(multiPartFile, scriptAttachmentMetadata);
    return ResponseEntity
        .created(new URI(UriUtils.encodePath(scriptAttachmentMetadata.getId(), "UTF-8")))
        .body(null);
  }

  /**
   * Load all attachment metadata objects for the given analysis package id.
   *
   * @param analysisPackageId The id of an {@link AnalysisPackage}.
   * @return A list of metadata objects.
   */
  @RequestMapping(path = "/analysis-packages/{analysisPackageId}/scripts/attachments",
      method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> findByAnalysisPackageId(
      @PathVariable("analysisPackageId") String analysisPackageId) {
    List<ScriptAttachmentMetadata> metadata =
        scriptAttachmentService.findAllByAnalysisPackage(analysisPackageId);
    return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(metadata);
  }

  /**
   * Delete all script attachments of the given {@link AnalysisPackage}.
   *
   * @param analysisPackageId The id of the {@link AnalysisPackage}.
   */
  @RequestMapping(path = "/analysis-packages/{analysisPackageId}/scripts/attachments",
      method = RequestMethod.DELETE)
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public ResponseEntity<?> deleteAllByAnalysisPackageId(
      @PathVariable("analysisPackageId") String analysisPackageId) {
    scriptAttachmentService.deleteAllByAnalysisPackageId(analysisPackageId);
    return ResponseEntity.noContent().build();
  }

  /**
   * Delete the given attachments of the given {@link Script}.
   *
   * @param analysisPackageId The analysisPackageId of the {@link Script}
   * @param scriptUuid The uuid of the {@link Script}
   * @param filename The filename of the {@link ScriptAttachmentMetadata}
   */
  @RequestMapping(path = "/analysis-packages/{analysisPackageId}/scripts/{scriptUuid}/"
      + "attachments/{filename:.+}", method = RequestMethod.DELETE)
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public ResponseEntity<?> delete(@PathVariable("analysisPackageId") String analysisPackageId,
      @PathVariable("scriptUuid") String scriptUuid, @PathVariable("filename") String filename) {
    scriptAttachmentService.deleteByAnalysisPackageIdAndScriptUuidAndFilename(analysisPackageId,
        scriptUuid, filename);
    return ResponseEntity.noContent().build();
  }
}

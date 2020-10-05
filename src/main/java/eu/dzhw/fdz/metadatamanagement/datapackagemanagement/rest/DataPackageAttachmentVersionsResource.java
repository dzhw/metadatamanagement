package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.rest;

import java.util.List;

import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackageAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.service.DataPackageAttachmentVersionsService;
import lombok.RequiredArgsConstructor;

/**
 * Rest Controller for retrieving previous version of the {@link DataPackageAttachmentMetadata}
 * domain object.
 * 
 * @author René Reitmann
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DataPackageAttachmentVersionsResource {

  private final DataPackageAttachmentVersionsService dataPackageAttachmentVersionsService;

  /**
   * Get the previous 10 versions of the dataPackage attachment metadata.
   * 
   * @param dataPackageId The id of the dataPackage
   * @param filename The filename of the attachment
   * @param limit like page size
   * @param skip for skipping n versions
   * 
   * @return A list of previous dataPackage versions
   */
  @GetMapping("/data-packages/{dataPackageId}/attachments/{filename:.+}/versions")
  public ResponseEntity<?> findPreviousDataPackageAttachmentVersions(
      @PathVariable String dataPackageId, @PathVariable String filename,
      @RequestParam(name = "limit", defaultValue = "5") Integer limit,
      @RequestParam(name = "skip", defaultValue = "0") Integer skip) {
    List<DataPackageAttachmentMetadata> dataPackageAttachmentVersions =
        dataPackageAttachmentVersionsService
            .findPreviousDataPackageAttachmentVersions(dataPackageId, filename, limit, skip);

    if (dataPackageAttachmentVersions == null) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok().cacheControl(CacheControl.noStore())
        .body(dataPackageAttachmentVersions);
  }
}

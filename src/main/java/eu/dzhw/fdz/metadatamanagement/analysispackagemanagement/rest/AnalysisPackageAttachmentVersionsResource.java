package eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.rest;

import java.util.List;

import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.AnalysisPackageAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.service.AnalysisPackageAttachmentVersionsService;
import lombok.RequiredArgsConstructor;

/**
 * Rest Controller for retrieving previous version of the {@link AnalysisPackageAttachmentMetadata}
 * domain object.
 * 
 * @author René Reitmann
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AnalysisPackageAttachmentVersionsResource {

  private final AnalysisPackageAttachmentVersionsService analysisPackageAttachmentVersionsService;

  /**
   * Get the previous 10 versions of the analysis package attachment metadata.
   * 
   * @param analysisPackageId The id of the analysisPackage
   * @param filename The filename of the attachment
   * @param limit like page size
   * @param skip for skipping n versions
   * 
   * @return A list of previous analysisPackage versions
   */
  @GetMapping("/analysis-packages/{analysisPackageId}/attachments/{filename:.+}/versions")
  public ResponseEntity<?> findPreviousAnalysisPackageAttachmentVersions(
      @PathVariable String analysisPackageId, @PathVariable String filename,
      @RequestParam(name = "limit", defaultValue = "5") Integer limit,
      @RequestParam(name = "skip", defaultValue = "0") Integer skip) {
    List<AnalysisPackageAttachmentMetadata> analysisPackageAttachmentVersions =
        analysisPackageAttachmentVersionsService.findPreviousAnalysisPackageAttachmentVersions(
            analysisPackageId, filename, limit, skip);

    if (analysisPackageAttachmentVersions == null) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok().cacheControl(CacheControl.noStore())
        .body(analysisPackageAttachmentVersions);
  }
}

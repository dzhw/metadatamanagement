package eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.rest;

import java.util.List;

import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.AnalysisPackage;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.service.AnalysisPackageVersionsService;
import lombok.RequiredArgsConstructor;

/**
 * Rest Controller for retrieving previous version of the analysis package domain object.
 * 
 * @author René Reitmann
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AnalysisPackageVersionsResource {

  private final AnalysisPackageVersionsService analysisPackageVersionsService;

  /**
   * Get the previous 5 versions of the analysis package.
   * 
   * @param id The id of the analysis package
   * @param limit like page size
   * @param skip for skipping n versions
   * 
   * @return A list of previous analysis package versions
   */
  @GetMapping("/analysis-packages/{id}/versions")
  public ResponseEntity<?> findPreviousAnalysisPackageVersions(@PathVariable String id,
      @RequestParam(name = "limit", defaultValue = "5") Integer limit,
      @RequestParam(name = "skip", defaultValue = "0") Integer skip) {
    List<AnalysisPackage> analysisPackageVersions =
        analysisPackageVersionsService.findPreviousVersions(id, limit, skip);

    return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(analysisPackageVersions);
  }
}

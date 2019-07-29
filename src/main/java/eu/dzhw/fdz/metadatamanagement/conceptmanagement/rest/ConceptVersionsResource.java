package eu.dzhw.fdz.metadatamanagement.conceptmanagement.rest;

import java.util.List;

import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.Concept;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.service.ConceptVersionsService;
import lombok.RequiredArgsConstructor;

/**
 * Rest Controller for retrieving previous version of a {@link Concept}.
 * 
 * @author René Reitmann
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ConceptVersionsResource {
  
  private final ConceptVersionsService conceptVersionsService;
    
  /**
   * Get the previous 5 versions of the {@link Concept}.
   * 
   * @param id The id of the {@link Concept}
   * @param limit like page size
   * @param skip for skipping n versions
   * 
   * @return A list of previous concept versions
   */
  @GetMapping("/concepts/{id}/versions")
  public ResponseEntity<?> findPreviousConceptVersions(@PathVariable String id,
      @RequestParam(name = "limit", defaultValue = "5") Integer limit,
      @RequestParam(name = "skip", defaultValue = "0") Integer skip) {
    List<Concept> conceptVersions = conceptVersionsService.findPreviousVersions(id, limit, skip);
    
    if (conceptVersions == null) {
      return ResponseEntity.notFound().build();
    }
    
    return ResponseEntity.ok()
        .cacheControl(CacheControl.noStore())
        .body(conceptVersions);
  }
}

package eu.dzhw.fdz.metadatamanagement.studymanagement.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.javers.core.Javers;
import org.javers.repository.jql.QueryBuilder;
import org.javers.shadow.Shadow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;

/**
 * Rest Controller for retrieving previous version of the study domain object.
 * 
 * @author René Reitmann
 */
@RestController
@RequestMapping("/api")
public class StudyVersionsResource {
  
  @Autowired
  private Javers javers;
  
  /**
   * Get the previous 10 versions of the study.
   * 
   * @param id The id of the study
   * @param limit like page size
   * @param skip for skipping n versions
   * 
   * @return A list of previous study versions
   */
  @RequestMapping("/studies/{id}/versions")
  public ResponseEntity<?> findPreviousStudyVersions(@PathVariable String id,
      @RequestParam(name = "limit", defaultValue = "10") Integer limit,
      @RequestParam(name = "skip", defaultValue = "1") Integer skip) {
    QueryBuilder jqlQuery =
        QueryBuilder.byInstanceId(id, Study.class)
        .withChildValueObjects()
        .limit(limit).skip(skip);

    List<Shadow<Study>> previousVersions = javers.findShadows(jqlQuery.build());

    return ResponseEntity.ok()
        .cacheControl(CacheControl.noStore())
        .body(previousVersions.stream().map(shadow -> shadow.get()).collect(Collectors.toList()));
  }
}

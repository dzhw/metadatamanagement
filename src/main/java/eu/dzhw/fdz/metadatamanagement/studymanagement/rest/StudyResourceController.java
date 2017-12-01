package eu.dzhw.fdz.metadatamanagement.studymanagement.rest;

import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.studymanagement.repository.StudyRepository;

/**
 * Study REST Controller which overrides default spring data rest methods.
 * 
 * @author René Reitmann
 */
@RepositoryRestController
public class StudyResourceController {

  @Autowired
  private StudyRepository studyRepository;

  /**
   * Override default get by id since it does not set cache headers correctly.
   * 
   * @param id a study id
   * @return the study or not found
   */
  @RequestMapping(method = RequestMethod.GET, value = "/studies/{id}")
  public ResponseEntity<Study> findStudy(@PathVariable String id) {
    Study study = studyRepository.findOne(id);

    if (study == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok()
        .cacheControl(CacheControl.maxAge(0, TimeUnit.DAYS).mustRevalidate().cachePublic())
        .eTag(study.getVersion().toString())
        .lastModified(
            study.getLastModifiedDate().atZone(ZoneId.of("GMT")).toInstant().toEpochMilli())
        .body(study);
  }
}

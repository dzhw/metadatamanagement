package eu.dzhw.fdz.metadatamanagement.studymanagement.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.studymanagement.repository.StudyRepository;

/**
 * Study REST Controller which overrides default spring data rest methods.
 * 
 * @author René Reitmann
 */
@RestController("/study-serieses")
public class StudySeriesesResourceController {

  private StudyRepository studyRepository;
  
  @Autowired
  public StudySeriesesResourceController(StudyRepository studyRepository) {
    this.studyRepository = studyRepository;
  }

  /**
   * Get all available study serieses.
   */
  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<List<I18nString>> findAllStudySerieses() {
    List<I18nString> studySerieses = studyRepository.findAllStudySerieses();
    return ResponseEntity.ok().cacheControl(CacheControl.noStore())
        .body(studySerieses);
  }
}

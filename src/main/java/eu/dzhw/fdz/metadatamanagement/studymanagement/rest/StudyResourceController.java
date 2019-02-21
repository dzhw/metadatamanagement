package eu.dzhw.fdz.metadatamanagement.studymanagement.rest;

import eu.dzhw.fdz.metadatamanagement.common.rest.GenericShadowableDomainObjectResourceController;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.studymanagement.repository.StudyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * Study REST Controller which overrides default spring data rest methods.
 * 
 * @author René Reitmann
 */
@RepositoryRestController
public class StudyResourceController 
    extends GenericShadowableDomainObjectResourceController<Study, StudyRepository> {

  @Autowired
  public StudyResourceController(StudyRepository studyRepository,
                                 ApplicationEventPublisher applicationEventPublisher) {
    super(studyRepository, applicationEventPublisher);
  }

  /**
   * Override default get by id since it does not set cache headers correctly.
   * 
   * @param id a study id
   * @return the study or not found
   */
  @RequestMapping(method = RequestMethod.GET, value = "/studies/{id:.+}")
  public ResponseEntity<Study> findStudy(@PathVariable String id) {
    return super.findDomainObject(id);
  }

  /**
   * Override default put to prevent shadow copy updates or creating new ones.
   * @param id Study id
   * @param study Study
   */
  @RequestMapping(method = RequestMethod.PUT, value = "/studies/{id:.+}")
  public ResponseEntity<?> putStudy(@PathVariable String id, @RequestBody Study study) {
    return super.putDomainObject(id, study);
  }

  /**
   * Override default delete to prevent shadow copy deletion.
   * @param id Study id
   */
  @RequestMapping(method = RequestMethod.DELETE, value = "/studies/{id:.+}")
  public ResponseEntity<?> deleteStudy(@PathVariable String id) {
    return super.deleteDomainObject(id);
  }

  @Override
  protected URI buildLocationHeaderUri(Study domainObject) {
    return UriComponentsBuilder.fromPath("/api/studies/" + domainObject.getId()).build().toUri();
  }
}

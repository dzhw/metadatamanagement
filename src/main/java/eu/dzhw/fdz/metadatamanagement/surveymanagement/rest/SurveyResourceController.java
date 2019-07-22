package eu.dzhw.fdz.metadatamanagement.surveymanagement.rest;

import eu.dzhw.fdz.metadatamanagement.common.rest.OldGenericShadowableDomainObjectResourceController;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

/**
 * Survey REST Controller which overrides default spring data rest methods.
 * 
 * @author René Reitmann
 */
@RepositoryRestController
public class SurveyResourceController 
    extends OldGenericShadowableDomainObjectResourceController<Survey, SurveyRepository> {

  @Autowired
  public SurveyResourceController(SurveyRepository surveyRepository,
                                  ApplicationEventPublisher applicationEventPublisher) {
    super(surveyRepository, applicationEventPublisher);
  }

  /**
   * Override default get by id since it does not set cache headers correctly.
   * 
   * @param id a survey id
   * @return the survey or not found
   */
  @RequestMapping(method = RequestMethod.GET, value = "/surveys/{id:.+}")
  public ResponseEntity<Survey> findSurvey(@PathVariable String id) {
    return super.findDomainObject(id); 
  }

  /**
   * Override default post to prevent clients from creating shadow copies.
   * @param survey Survey
   */
  @RequestMapping(method = RequestMethod.POST, value = "/surveys")
  public ResponseEntity<?> postSurvey(@Valid @RequestBody Survey survey) {
    return super.postDomainObject(survey);
  }

  /**
   * Override default put to prevent creating or updating shadow copies.
   * @param id Survey id
   * @param survey Survey
   */
  @RequestMapping(method = RequestMethod.PUT, value = "/surveys/{id:.+}")
  public ResponseEntity<?> putSurvey(@PathVariable String id, @Valid @RequestBody Survey survey) {
    return super.putDomainObject(id, survey);
  }

  /**
   * Override default delete to prevent clients from deleting shadow copies.
   * @param id Survey id
   */
  @RequestMapping(method = RequestMethod.DELETE, value = "/surveys/{id:.+}")
  public ResponseEntity<?> deleteSurvey(@PathVariable String id) {
    return super.deleteDomainObject(id);
  }

  @Override
  protected URI buildLocationHeaderUri(Survey domainObject) {
    return UriComponentsBuilder.fromPath("/api/surveys/" + domainObject.getId()).build()
        .toUri();
  }
}

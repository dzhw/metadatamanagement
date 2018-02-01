package eu.dzhw.fdz.metadatamanagement.surveymanagement.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import eu.dzhw.fdz.metadatamanagement.common.rest.GenericDomainObjectResourceController;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;

/**
 * Survey REST Controller which overrides default spring data rest methods.
 * 
 * @author René Reitmann
 */
@RepositoryRestController
public class SurveyResourceController 
    extends GenericDomainObjectResourceController<Survey, SurveyRepository> {

  @Autowired
  public SurveyResourceController(SurveyRepository surveyRepository) {
    super(surveyRepository);
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
}

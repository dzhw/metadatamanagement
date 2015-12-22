package eu.dzhw.fdz.metadatamanagement.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import eu.dzhw.fdz.metadatamanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.service.SurveyService;
import eu.dzhw.fdz.metadatamanagement.web.rest.util.HeaderUtil;
import eu.dzhw.fdz.metadatamanagement.web.rest.util.PaginationUtil;

/**
 * REST controller for managing Survey.
 */
@RestController
@RequestMapping("/api")
public class SurveyResource {

  private final Logger log = LoggerFactory.getLogger(SurveyResource.class);

  @Inject
  private SurveyService surveyService;

  /**
   * POST /surveys -> Create a new survey.
   */
  @RequestMapping(value = "/surveys", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<Survey> createSurvey(@Valid @RequestBody Survey survey)
      throws URISyntaxException {
    log.debug("REST request to save Survey : {}", survey);

    Survey result = surveyService.createSurvey(survey);
    return ResponseEntity.created(new URI("/api/surveys/" + result.getId()))
      .headers(HeaderUtil.createEntityCreationAlert("survey", result.getId()))
      .body(result);
  }

  /**
   * PUT /surveys -> Updates an existing survey.
   */
  @RequestMapping(value = "/surveys", method = RequestMethod.PUT,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<Survey> updateSurvey(@Valid @RequestBody Survey survey)
      throws URISyntaxException {
    log.debug("REST request to update Survey : {}", survey);

    Survey result = surveyService.updateSurvey(survey);
    return ResponseEntity.ok()
      .headers(HeaderUtil.createEntityUpdateAlert("survey", survey.getId()))
      .body(result);
  }

  /**
   * GET /surveys -> get all the surveys.
   */
  @RequestMapping(value = "/surveys", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<List<Survey>> getAllSurveys(Pageable pageable,
      @RequestParam Optional<String> getAll, @RequestParam Optional<String> fdzProjectName)
          throws URISyntaxException {
    log.debug("REST request to get a page of Surveys");
    if (getAll.isPresent()) {
      List<Survey> allSurveys = surveyService.findAll();
      return new ResponseEntity<>(allSurveys, HttpStatus.OK);
    } else {
      if (fdzProjectName.isPresent()) {
        List<Survey> allSurveysByFdzName =
            surveyService.findAllWithFdzProjectName(fdzProjectName.get());
        this.log.debug("Found surveys {}", allSurveysByFdzName.size());
        return new ResponseEntity<>(allSurveysByFdzName, HttpStatus.OK);
      } else {
        Page<Survey> page = surveyService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/surveys");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
      }
    }
  }

  /**
   * GET /surveys/:id -> get the "id" survey.
   */
  @RequestMapping(value = "/surveys/{id}", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<Survey> getSurvey(@PathVariable String id) {
    log.debug("REST request to get Survey : {}", id);
    Survey survey = surveyService.findOne(id);
    return Optional.ofNullable(survey)
      .map(result -> new ResponseEntity<>(result, HttpStatus.OK))
      .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  // TODO del?
  // /**
  // * GET /surveys/:fdzProjectName/ -> get survey by a given fdz project name.
  // */
  // @RequestMapping(value = "/surveys/{fdzProject}", method = RequestMethod.GET,
  // produces = MediaType.APPLICATION_JSON_VALUE)
  // @Timed
  // public ResponseEntity<List<Survey>> getSurveyByFdzProjectName(
  // @PathVariable FdzProject fdzProjectName) {
  // this.log.debug("REST request all Survey by a given FdzProject : {}", fdzProjectName);
  // List<Survey> allSurveysByFdzName =
  // surveyService.findAllWithFdzProjectName(fdzProjectName.getName());
  // this.log.debug("Found surveys {}", allSurveysByFdzName.size());
  // return new ResponseEntity<>(allSurveysByFdzName, HttpStatus.OK);
  // }

  /**
   * DELETE /surveys/:id -> delete the "id" survey.
   */
  @RequestMapping(value = "/surveys/{id}", method = RequestMethod.DELETE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<Void> deleteSurvey(@PathVariable String id) {
    log.debug("REST request to delete Survey : {}", id);
    surveyService.delete(id);
    return ResponseEntity.ok()
      .headers(HeaderUtil.createEntityDeletionAlert("survey", id))
      .build();
  }
}

package eu.dzhw.fdz.metadatamanagement.service;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.repository.SurveyRepository;
import eu.dzhw.fdz.metadatamanagement.service.exception.EntityExistsException;
import eu.dzhw.fdz.metadatamanagement.service.exception.EntityNotFoundException;

/**
 * Service Implementation for managing Survey.
 */
@Service
public class SurveyService {

  private final Logger log = LoggerFactory.getLogger(SurveyService.class);

  @Inject
  private SurveyRepository surveyRepository;

  /**
   * Create a survey in mongo and elasticsearch.
   * 
   * @param survey The survey to create.
   * @return The created survey.
   * @throws EntityExistsException if there is already a survey with the given id
   */
  public Survey createSurvey(Survey survey) {
    try {
      survey = surveyRepository.insert(survey);
    } catch (DuplicateKeyException e) {
      throw new EntityExistsException(Survey.class, survey.getId());
    }
    // TODO create search index for projects
    // updateSearchIndices(fdzProject);
    return survey;
  }

  /**
   * Update an existing survey in mongo and elasticsearch.
   * 
   * @param survey the survey to update.
   * @return The updated survey.
   * @throws EntityNotFoundException if there is no survey with the given id (name)
   */
  public Survey updateSurvey(Survey survey) {
    if (!surveyRepository.exists(survey.getId())) {
      throw new EntityNotFoundException(Survey.class, survey.getId());
    }
    survey = surveyRepository.save(survey);
    // TODO create search index for projects
    // updateSearchIndices(fdzProject);
    return survey;
  }

  /**
   * get all the surveys.
   * 
   * @return the list of entities
   */
  public Page<Survey> findAll(Pageable pageable) {
    log.debug("Request to get all Surveys");
    Page<Survey> result = surveyRepository.findAll(pageable);
    return result;
  }

  /**
   * get one survey by id.
   * 
   * @return the entity
   */
  public Survey findOne(String id) {
    log.debug("Request to get Survey : {}", id);
    Survey survey = surveyRepository.findOne(id);
    return survey;
  }

  /**
   * delete the survey by id.
   */
  public void delete(String id) {
    log.debug("Request to delete Survey : {}", id);
    surveyRepository.delete(id);
  }
}

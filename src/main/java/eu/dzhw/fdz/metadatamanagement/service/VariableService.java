package eu.dzhw.fdz.metadatamanagement.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.data.rest.core.event.AfterDeleteEvent;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.domain.FdzProject;
import eu.dzhw.fdz.metadatamanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.repository.VariableRepository;
import eu.dzhw.fdz.metadatamanagement.search.VariableSearchDao;

/**
 * Service for creating and updating variable. Used for updating variables in mongo and
 * elasticsearch.
 * 
 * @author René Reitmann
 * @author Daniel Katzberg
 */
@Service
@RepositoryEventHandler
public class VariableService {

  @Inject
  private VariableRepository variableRepository;

  @Inject
  private VariableSearchDao variableSearchDao;
  
  @Inject
  private ApplicationEventPublisher eventPublisher;

  /**
   * Load all variables from mongo and update the search indices.
   */
  public void reindexAllVariables() {
    Pageable pageable = new PageRequest(0, 50);
    Page<Variable> variables = variableRepository.findAll(pageable);

    while (variables.hasContent()) {
      variableSearchDao.index(variables.getContent());
      variables = variableRepository.findAll(pageable.next());
    }
  }
  
  /**
   * Delete all variables when the survey was deleted.
   * 
   * @param survey the survey which has been deleted.
   */
  @HandleAfterDelete
  public void onSurveyDeleted(Survey survey) {
    List<Variable> deletedVariables = variableRepository.deleteBySurvey(survey);
    deletedVariables.forEach(
        variable -> eventPublisher.publishEvent(new AfterDeleteEvent(variable)));
  }
  
  /**
   * Delete all variables when the fdzProject was deleted.
   * 
   * @param fdzProject the fdzProject which has been deleted.
   */
  @HandleAfterDelete
  public void onFdzProjectDeleted(FdzProject fdzProject) {
    List<Variable> deletedVariables = variableRepository.deleteByFdzProject(fdzProject);
    deletedVariables.forEach(
        variable -> eventPublisher.publishEvent(new AfterDeleteEvent(variable)));
  }
}

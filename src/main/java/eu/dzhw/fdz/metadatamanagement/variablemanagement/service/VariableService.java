package eu.dzhw.fdz.metadatamanagement.variablemanagement.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.ElasticsearchUpdateQueueAction;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.repository.VariableRepository;

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
  private ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;

  /**
   * Delete all variables when the dataAcquisitionProject was deleted.
   * 
   * @param dataAcquisitionProject the dataAcquisitionProject which has been deleted.
   */
  @HandleAfterDelete
  public void onDataAcquisitionProjectDeleted(DataAcquisitionProject dataAcquisitionProject) {
    deleteAllVariablesByProjectId(dataAcquisitionProject.getId());
  }
  
  /**
   * A service method for deletion of variables within a data acquisition project.
   * @param dataAcquisitionProjectId the id for to the data acquisition project.
   */
  public void deleteAllVariablesByProjectId(String dataAcquisitionProjectId) {
    List<Variable> deletedVariables =
        variableRepository.deleteByDataAcquisitionProjectId(dataAcquisitionProjectId);
    deletedVariables.forEach(variable -> {
      elasticsearchUpdateQueueService.enqueue(
          variable.getId(), 
          ElasticsearchType.variables, 
          ElasticsearchUpdateQueueAction.DELETE);      
    });
  }
  
  /**
   * Enqueue deletion of variable search document when the variable is deleted.
   * 
   * @param variable the deleted variable.
   */
  @HandleAfterDelete
  public void onVariableDeleted(Variable variable) {
    elasticsearchUpdateQueueService.enqueue(
        variable.getId(), 
        ElasticsearchType.variables, 
        ElasticsearchUpdateQueueAction.DELETE);
  }
  
  /**
   * Enqueue update of variable search document when the variable is updated.
   * 
   * @param variable the updated or created variable.
   */
  @HandleAfterCreate
  @HandleAfterSave
  public void onVariableSaved(Variable variable) {
    elasticsearchUpdateQueueService.enqueue(
        variable.getId(), 
        ElasticsearchType.variables, 
        ElasticsearchUpdateQueueAction.UPSERT);
  }
  
  /**
   * Enqueue update of variable search document when the survey is updated.
   * 
   * @param survey the updated or created survey.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onSurveySaved(Survey survey) {
    List<Variable> variables = variableRepository.findBySurveyIdsContaining(survey.getId());
    variables.forEach(variable -> {
      elasticsearchUpdateQueueService.enqueue(
          variable.getId(), 
          ElasticsearchType.variables, 
          ElasticsearchUpdateQueueAction.UPSERT);      
    });
  }
  
  /**
   * Enqueue update of variable search document when the dataSet is updated.
   * 
   * @param dataSet the updated or created dataSet.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onDataSetSaved(DataSet dataSet) {
    List<Variable> variables = variableRepository.findByIdIn(dataSet.getVariableIds());
    variables.forEach(variable -> {
      elasticsearchUpdateQueueService.enqueue(
          variable.getId(), 
          ElasticsearchType.variables, 
          ElasticsearchUpdateQueueAction.UPSERT);      
    });
  }
  
}

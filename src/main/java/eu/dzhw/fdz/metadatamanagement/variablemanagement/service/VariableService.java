package eu.dzhw.fdz.metadatamanagement.variablemanagement.service;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.data.rest.core.event.AfterDeleteEvent;
import org.springframework.data.rest.core.event.BeforeDeleteEvent;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyService;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.ProjectReleasedEvent;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.service.RelatedPublicationChangesProvider;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.ElasticsearchUpdateQueueAction;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
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

  @Autowired
  private VariableRepository variableRepository;
  
  @Autowired
  private VariableChangesProvider variableChangesProvider;
  
  @Autowired
  private ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;
  
  @Autowired
  private ApplicationEventPublisher eventPublisher;

  @Autowired
  private RelatedPublicationChangesProvider relatedPublicationChangesProvider;

  @Autowired
  private ShadowCopyService<Variable> shadowCopyService;

  @Autowired
  private VariableShadowCopyDataSource variableShadowCopyDataProvider;

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
   * Update all variables of the project, when the project is released.
   * 
   * @param dataAcquisitionProject the changed project
   */
  @HandleAfterSave
  public void onDataAcquisitionProjectUpdated(DataAcquisitionProject dataAcquisitionProject) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> variableRepository.streamIdsByDataAcquisitionProjectId(
            dataAcquisitionProject.getId()),
        ElasticsearchType.variables);
  }

  /**
   * Create shadow variables for instruments on project release.
   * @param projectReleasedEvent Released project event
   */
  @EventListener
  public void onProjectRelease(ProjectReleasedEvent projectReleasedEvent) {
    shadowCopyService.createShadowCopies(projectReleasedEvent.getDataAcquisitionProject(),
        projectReleasedEvent.getPreviousReleaseVersion(), variableShadowCopyDataProvider);
  }
  
  /**
   * A service method for deletion of variables within a data acquisition project.
   * @param dataAcquisitionProjectId the id for to the data acquisition project.
   */
  public void deleteAllVariablesByProjectId(String dataAcquisitionProjectId) {
    try (Stream<Variable> variables = variableRepository
        .streamByDataAcquisitionProjectId(dataAcquisitionProjectId)) {
      variables.forEach(variable -> {
        eventPublisher.publishEvent(new BeforeDeleteEvent(variable));
        variableRepository.delete(variable);
        eventPublisher.publishEvent(new AfterDeleteEvent(variable));
      });
    }
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
   * Remember the old and new variable.
   * 
   * @param variable the new variable
   */
  @HandleBeforeSave
  public void onBeforeVariableSaved(Variable variable) {
    variableChangesProvider.put(variable, 
        variableRepository.findById(variable.getId()).orElse(null));
  }

  @HandleBeforeCreate
  public void onBeforeVariableCreated(Variable variable) {
    variableChangesProvider.put(variable, null);
  }

  @HandleBeforeDelete
  public void onBeforeVariableDeleted(Variable variable) {
    variableChangesProvider.put(null, variable);
  }
  
  /**
   * Enqueue update of variable search documents when the data set is changed.
   * 
   * @param dataSet the updated, created or deleted data set.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onDataSetChanged(DataSet dataSet) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> variableRepository.streamIdsByDataSetId(dataSet.getId()),
        ElasticsearchType.variables);
  }
  
  /**
   * Enqueue update of variable search documents when the study is changed.
   * 
   * @param study the updated, created or deleted study.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onStudyChanged(Study study) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> variableRepository.streamIdsByStudyId(study.getId()),
        ElasticsearchType.variables);
  }
  
  /**
   * Enqueue update of variable search documents when the related publication is changed.
   * 
   * @param relatedPublication the updated, created or deleted related publication.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onRelatedPublicationChanged(RelatedPublication relatedPublication) {
    List<String> variableIds = relatedPublicationChangesProvider
        .getAffectedVariableIds(relatedPublication.getId());
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> variableRepository.streamIdsByIdIn(variableIds),
        ElasticsearchType.variables);

    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> variableRepository
            .streamIdsByMasterIdInAndShadowIsTrueAndSuccessorIdIsNull(variableIds),
        ElasticsearchType.variables);
  }
  
  /**
   * Enqueue update of variable search documents when the instrument is changed.
   * 
   * @param instrument the updated, created or deleted instrument.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onInstrumentChanged(Instrument instrument) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> variableRepository.streamIdsByRelatedQuestionsInstrumentId(
            instrument.getId()),
        ElasticsearchType.variables);
  }
  
  /**
   * Enqueue update of variable search documents when the survey is updated.
   * 
   * @param survey the updated, created or deleted survey.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onSurveyChanged(Survey survey) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> variableRepository.streamIdsBySurveyIdsContaining(
            survey.getId()),
        ElasticsearchType.variables);
  }
}

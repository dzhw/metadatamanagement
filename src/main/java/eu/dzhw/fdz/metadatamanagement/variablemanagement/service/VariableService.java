package eu.dzhw.fdz.metadatamanagement.variablemanagement.service;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.data.rest.core.event.AfterDeleteEvent;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
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
  private ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;
  
  @Autowired
  private ApplicationEventPublisher eventPublisher;

  /**
   * Delete all variables when the dataAcquisitionProject was deleted.
   * 
   * @param dataAcquisitionProject the dataAcquisitionProject which has been deleted.
   */
  @HandleAfterDelete
  public void onDataAcquisitionProjectDeleted(DataAcquisitionProject dataAcquisitionProject) {
    deleteAllVariablesByProjectId(dataAcquisitionProject.getId());
  }
  
  @HandleAfterSave
  public void onDataAcquisitionProjectUpdated(DataAcquisitionProject dataAcquisitionProject) {
    enqueueUpserts(variableRepository
        .streamIdsByDataAcquisitionProjectId(dataAcquisitionProject.getId()));
  }
  
  /**
   * A service method for deletion of variables within a data acquisition project.
   * @param dataAcquisitionProjectId the id for to the data acquisition project.
   */
  public void deleteAllVariablesByProjectId(String dataAcquisitionProjectId) {
    try (Stream<Variable> variables = variableRepository
        .streamByDataAcquisitionProjectId(dataAcquisitionProjectId)) {
      variables.forEach(variable -> {
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
   * Enqueue update of variable search documents when the data set is changed.
   * 
   * @param dataSet the updated, created or deleted data set.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onDataSetChanged(DataSet dataSet) {
    enqueueUpserts(variableRepository.streamIdsByDataSetId(dataSet.getId()));
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
    enqueueUpserts(variableRepository.streamIdsByStudyId(study.getId()));
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
    enqueueUpserts(variableRepository
        .streamIdsByIdIn(relatedPublication.getVariableIds()));
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
    enqueueUpserts(variableRepository.streamIdsByRelatedQuestionsInstrumentId(
        instrument.getId()));
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
    enqueueUpserts(variableRepository.streamIdsBySurveyIdsContaining(survey.getId()));
  }
  
  private void enqueueUpserts(Stream<IdAndVersionProjection> variables) {
    try (Stream<IdAndVersionProjection> variableStream = variables) {
      variableStream.forEach(variable -> {
        elasticsearchUpdateQueueService.enqueue(variable.getId(),
            ElasticsearchType.variables, ElasticsearchUpdateQueueAction.UPSERT);
      });      
    }
  }
}

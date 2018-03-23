package eu.dzhw.fdz.metadatamanagement.datasetmanagement.service;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
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

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.service.RelatedPublicationChangesProvider;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.ElasticsearchUpdateQueueAction;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;

/**
 * This service for {@link DataSet} will wait for delete events of a survey or a data acquisition
 * project.
 * 
 * @author Daniel Katzberg
 *
 */
@Service
@RepositoryEventHandler
public class DataSetService {

  @Autowired
  private DataSetRepository dataSetRepository;
  
  @Autowired
  private DataSetChangesProvider dataSetChangesProvider;

  @Autowired
  private ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;
  
  @Autowired
  private ApplicationEventPublisher eventPublisher;
  
  @Autowired
  private RelatedPublicationChangesProvider relatedPublicationChangesProvider;
  
  @Autowired 
  private DataSetAttachmentService dataSetAttachmentService;

  /**
   * Delete all data sets when the dataAcquisitionProject was deleted.
   * 
   * @param dataAcquisitionProject the dataAcquisitionProject which has been deleted.
   */
  @HandleAfterDelete
  public void onDataAcquisitionProjectDeleted(DataAcquisitionProject dataAcquisitionProject) {
    deleteDataSetsByProjectId(dataAcquisitionProject.getId());
  }
  
  /**
   * Update the {@link DataSet}s of the project when the project is released.
   * 
   * @param dataAcquisitionProject the changed project
   */
  @HandleAfterSave
  public void onDataAcquisitionProjectUpdated(DataAcquisitionProject dataAcquisitionProject) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(dataSetRepository
        .streamIdsByDataAcquisitionProjectId(dataAcquisitionProject.getId()),
        ElasticsearchType.data_sets);
  }
  
  /**
   * A service method for deletion of dataSets within a data acquisition project.
   * @param dataAcquisitionProjectId the id for to the data acquisition project.
   */
  private void deleteDataSetsByProjectId(String dataAcquisitionProjectId) {
    try (Stream<DataSet> dataSets = dataSetRepository
        .streamByDataAcquisitionProjectId(dataAcquisitionProjectId)) {
      dataSets.forEach(dataSet -> {
        eventPublisher.publishEvent(new BeforeDeleteEvent(dataSet));
        dataSetRepository.delete(dataSet);
        eventPublisher.publishEvent(new AfterDeleteEvent(dataSet));              
      });
    }
  }
  
  /**
   * Enqueue deletion of dataSet search document when the dataSet is deleted.
   * 
   * @param dataSet the deleted dataSet.
   */
  @HandleAfterDelete
  public void onDataSetDeleted(DataSet dataSet) {
    this.dataSetAttachmentService.deleteAllByDataSetId(dataSet.getId());
    elasticsearchUpdateQueueService.enqueue(
        dataSet.getId(), 
        ElasticsearchType.data_sets, 
        ElasticsearchUpdateQueueAction.DELETE);
  }
  
  /**
   * Enqueue update of dataSet search document when the dataSet is updated.
   * 
   * @param dataSet the updated or created dataSet.
   */
  @HandleAfterCreate
  @HandleAfterSave
  public void onDataSetSaved(DataSet dataSet) {
    elasticsearchUpdateQueueService.enqueue(
        dataSet.getId(), 
        ElasticsearchType.data_sets, 
        ElasticsearchUpdateQueueAction.UPSERT);
  }
  
  /**
   * Remember the old and new data set.
   * 
   * @param dataSet the new data set
   */
  @HandleBeforeSave
  public void onBeforeDataSetSaved(DataSet dataSet) {
    dataSetChangesProvider.put(dataSet, 
        dataSetRepository.findOne(dataSet.getId()));
  }
  
  @HandleBeforeCreate
  public void onBeforeDataSetCreated(DataSet dataSet) {
    dataSetChangesProvider.put(dataSet, null);
  }
  
  @HandleBeforeDelete
  public void onBeforeDataSetDeleted(DataSet dataSet) {
    dataSetChangesProvider.put(null, dataSet);
  }
  
  /**
   * Enqueue update of dataSet search documents when the study is changed.
   * 
   * @param study the updated, created or deleted study.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onStudyChanged(Study study) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(dataSetRepository
        .streamIdsByStudyId(study.getId()), ElasticsearchType.data_sets);
  }
  
  /**
   * Enqueue update of dataSet search documents when the survey is updated.
   * 
   * @param survey the updated, created or deleted survey.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onSurveyChanged(Survey survey) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(dataSetRepository
        .streamIdsBySurveyIdsContaining(survey.getId()), ElasticsearchType.data_sets);
  }
  
  /**
   * Enqueue update of dataSet search documents when the variable is changed.
   * 
   * @param variable the updated, created or deleted variable.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onVariableChanged(Variable variable) {
    IdAndVersionProjection dataSet = dataSetRepository.findOneIdById(variable.getDataSetId());
    if (dataSet != null) {
      elasticsearchUpdateQueueService.enqueueUpsertAsync(
          dataSet, 
          ElasticsearchType.data_sets);            
    }
  }
  
  /**
   * Enqueue update of dataSet search documents when the related publication is changed.
   * 
   * @param relatedPublication the updated, created or deleted related publication.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onRelatedPublicationChanged(RelatedPublication relatedPublication) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(dataSetRepository
        .streamIdsByIdIn(relatedPublicationChangesProvider
            .getAffectedDataSetIds(relatedPublication.getId())), ElasticsearchType.data_sets);
  }
}

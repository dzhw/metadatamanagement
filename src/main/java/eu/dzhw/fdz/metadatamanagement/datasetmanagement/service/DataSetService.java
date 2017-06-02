package eu.dzhw.fdz.metadatamanagement.datasetmanagement.service;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.data.rest.core.event.AfterDeleteEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
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
  private ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;
  
  @Autowired
  private ApplicationEventPublisher eventPublisher;
  
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
  
  @HandleAfterSave
  public void onDataAcquisitionProjectUpdated(DataAcquisitionProject dataAcquisitionProject) {
    enqueueUpserts(dataSetRepository
        .streamIdsByDataAcquisitionProjectId(dataAcquisitionProject.getId()));
  }
  
  /**
   * A service method for deletion of dataSets within a data acquisition project.
   * @param dataAcquisitionProjectId the id for to the data acquisition project.
   */
  private void deleteDataSetsByProjectId(String dataAcquisitionProjectId) {
    try (Stream<DataSet> dataSets = dataSetRepository
        .streamByDataAcquisitionProjectId(dataAcquisitionProjectId)) {
      dataSets.forEach(dataSet -> {
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
   * Enqueue update of dataSet search documents when the study is changed.
   * 
   * @param study the updated, created or deleted study.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  @Async
  public void onStudyChanged(Study study) {
    enqueueUpserts(dataSetRepository.streamIdsByStudyId(study.getId()));
  }
  
  /**
   * Enqueue update of dataSet search documents when the survey is updated.
   * 
   * @param survey the updated, created or deleted survey.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  @Async
  public void onSurveyChanged(Survey survey) {
    enqueueUpserts(dataSetRepository.streamIdsBySurveyIdsContaining(survey.getId()));
  }
  
  /**
   * Enqueue update of dataSet search documents when the variable is changed.
   * 
   * @param variable the updated, created or deleted variable.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  @Async
  public void onVariableChanged(Variable variable) {
    IdAndVersionProjection dataSet = dataSetRepository.findOneIdById(variable.getDataSetId());
    if (dataSet != null) {
      elasticsearchUpdateQueueService.enqueue(
          dataSet.getId(), 
          ElasticsearchType.data_sets, 
          ElasticsearchUpdateQueueAction.UPSERT);            
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
  @Async
  public void onRelatedPublicationChanged(RelatedPublication relatedPublication) {
    enqueueUpserts(dataSetRepository.streamIdsByIdIn(relatedPublication.getDataSetIds()));
  }
  
  private void enqueueUpserts(Stream<IdAndVersionProjection> dataSets) {
    try (Stream<IdAndVersionProjection> dataSetStream = dataSets) {
      dataSetStream.forEach(dataSet -> {
        elasticsearchUpdateQueueService.enqueue(dataSet.getId(),
            ElasticsearchType.data_sets, ElasticsearchUpdateQueueAction.UPSERT);
      });      
    }
  }
}

package eu.dzhw.fdz.metadatamanagement.datasetmanagement.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyService;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.ProjectReleasedEvent;
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

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.projections.IdAndNumberDataSetProjection;
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

  @Autowired
  private ShadowCopyService<DataSet> shadowCopyService;

  @Autowired
  private DataSetShadowCopyDataSource dataSetShadowCopyDataSource;

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
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> dataSetRepository.streamIdsByDataAcquisitionProjectId(
            dataAcquisitionProject.getId()),
        ElasticsearchType.data_sets);
  }
  
  /**
   * A service method for deletion of dataSets within a data acquisition project.
   * @param dataAcquisitionProjectId the id for to the data acquisition project.
   */
  public void deleteDataSetsByProjectId(String dataAcquisitionProjectId) {
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
        dataSetRepository.findById(dataSet.getId()).get());
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
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> dataSetRepository.streamIdsByStudyId(study.getId()),
        ElasticsearchType.data_sets);
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
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> dataSetRepository.streamIdsBySurveyIdsContaining(
            survey.getId()),
        ElasticsearchType.data_sets);
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
    elasticsearchUpdateQueueService.enqueueUpsertAsync(
        () -> dataSetRepository.findOneIdById(variable.getDataSetId()), 
        ElasticsearchType.data_sets);            
    
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
    List<String> dataSetIds = relatedPublicationChangesProvider.getAffectedDataSetIds(
        relatedPublication.getId()); 
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> dataSetRepository.streamIdsByIdIn(dataSetIds),
        ElasticsearchType.data_sets);
  }

  /**
   * Create shadow copies for instruments on project release.
   * @param projectReleasedEvent Released project event
   */
  @EventListener
  public void onProjectRelease(ProjectReleasedEvent projectReleasedEvent) {
    shadowCopyService.createShadowCopies(projectReleasedEvent.getDataAcquisitionProject(),
        dataSetShadowCopyDataSource);
  }

  /**
   * Get a list of available dataSet numbers for creating a new dataSet.
   * 
   * @param dataAcquisitionProjectId The project id.
   * @return A list of available dataSet numbers.
   */
  public List<Integer> getFreeDataSetNumbers(String dataAcquisitionProjectId) {
    List<Integer> result = new ArrayList<>();
    List<IdAndNumberDataSetProjection> existingNumbers =
        dataSetRepository.findDataSetNumbersByDataAcquisitionProjectId(dataAcquisitionProjectId);
    Optional<IdAndNumberDataSetProjection> max = existingNumbers.stream()
        .max((dataSet1, dataSet2) -> Integer.compare(dataSet1.getNumber(), dataSet2.getNumber()));
    if (!max.isPresent()) {
      result.add(1);
    } else {
      for (int i = 1; i < max.get().getNumber(); i++) {
        if (!dataSetNumberExists(existingNumbers, i)) {
          result.add(i);
        }
      }
      result.add(max.get().getNumber() + 1);
    }
    return result;
  }

  private boolean dataSetNumberExists(List<IdAndNumberDataSetProjection> dataSets, Integer number) {
    Predicate<IdAndNumberDataSetProjection> predicate =
        dataSet -> dataSet.getNumber().equals(number);
    return dataSets.stream().filter(predicate).findFirst().isPresent();
  }
}

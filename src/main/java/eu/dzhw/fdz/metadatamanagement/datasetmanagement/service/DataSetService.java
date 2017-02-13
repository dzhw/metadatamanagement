package eu.dzhw.fdz.metadatamanagement.datasetmanagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Service;

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
   * A service method for deletion of dataSets within a data acquisition project.
   * @param dataAcquisitionProjectId the id for to the data acquisition project.
   * @return List of deleted dataSets
   */
  public List<DataSet> deleteDataSetsByProjectId(String dataAcquisitionProjectId) {
    List<DataSet> deletedDataSets =
        this.dataSetRepository.deleteByDataAcquisitionProjectId(dataAcquisitionProjectId);
    deletedDataSets.forEach(dataSet -> {
      elasticsearchUpdateQueueService.enqueue(
          dataSet.getId(), 
          ElasticsearchType.data_sets, 
          ElasticsearchUpdateQueueAction.DELETE);      
    });
    return deletedDataSets;
  }
  
  /**
   * Enqueue deletion of dataSet search document when the dataSet is deleted.
   * 
   * @param dataSet the deleted dataSet.
   */
  @HandleAfterDelete
  public void onDataSetDeleted(DataSet dataSet) {
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
  public void onStudyChanged(Study study) {
    List<DataSet> dataSets = dataSetRepository.findByStudyId(study.getId());
    dataSets.forEach(dataSet -> {
      elasticsearchUpdateQueueService.enqueue(
          dataSet.getId(), 
          ElasticsearchType.data_sets, 
          ElasticsearchUpdateQueueAction.UPSERT);      
    });
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
    List<DataSet> dataSets = dataSetRepository.findBySurveyIdsContaining(survey.getId());
    dataSets.forEach(dataSet -> {
      elasticsearchUpdateQueueService.enqueue(
          dataSet.getId(), 
          ElasticsearchType.data_sets, 
          ElasticsearchUpdateQueueAction.UPSERT);      
    });
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
    DataSet dataSet = dataSetRepository.findOne(variable.getDataSetId());
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
  public void onRelatedPublicationChanged(RelatedPublication relatedPublication) {
    List<DataSet> dataSets = dataSetRepository.findByIdIn(relatedPublication.getDataSetIds());
    dataSets.forEach(dataSet -> {
      elasticsearchUpdateQueueService.enqueue(
          dataSet.getId(), 
          ElasticsearchType.data_sets, 
          ElasticsearchUpdateQueueAction.UPSERT);      
    });
  }
}

package eu.dzhw.fdz.metadatamanagement.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.data.rest.core.event.AfterDeleteEvent;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.repository.DataSetRepository;

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

  @Inject
  private DataSetRepository dataSetRepository;

  @Inject
  private ApplicationEventPublisher eventPublisher;


  /**
   * Delete all data sets when the survey was deleted.
   * 
   * @param survey the survey which has been deleted.
   */
  @HandleAfterDelete
  public void onSurveyDeleted(Survey survey) {
    List<DataSet> deletedDataSets = this.dataSetRepository.deleteBySurveyId(survey.getId());
    deletedDataSets.forEach(dataSet -> eventPublisher.publishEvent(new AfterDeleteEvent(dataSet)));
  }

  /**
   * Delete all data sets when the dataAcquisitionProject was deleted.
   * 
   * @param dataAcquisitionProject the dataAcquisitionProject which has been deleted.
   */
  @HandleAfterDelete
  public void onDataAcquisitionProjectDeleted(DataAcquisitionProject dataAcquisitionProject) {
    List<DataSet> deletedDataSets =
        this.dataSetRepository.deleteByDataAcquisitionProjectId(dataAcquisitionProject.getId());
    deletedDataSets.forEach(dataSet -> eventPublisher.publishEvent(new AfterDeleteEvent(dataSet)));
  }

}

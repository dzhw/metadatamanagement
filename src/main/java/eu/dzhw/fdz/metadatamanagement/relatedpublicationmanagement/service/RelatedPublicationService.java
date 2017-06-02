package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.service;

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
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.repository.RelatedPublicationRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.ElasticsearchUpdateQueueAction;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;

/**
 * This service for {@link RelatedPublicationService} will wait for delete events 
 * of a {@link RelatedPublication}.
 * 
 * @author Daniel Katzberg
 *
 */
@Service
@RepositoryEventHandler
public class RelatedPublicationService {

  @Autowired
  private RelatedPublicationRepository relatedPublicationRepository;

  @Autowired
  private ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;
  
  @Autowired
  private ApplicationEventPublisher eventPublisher;
  
  /**
   * A service method for deletion of relatedPublications within a data acquisition project.
   */
  public void deleteAll() {
    try (Stream<RelatedPublication> relatedPublications = relatedPublicationRepository
        .streamAllBy()) {
      relatedPublications.forEach(relatedPublication -> {
        relatedPublicationRepository.delete(relatedPublication);
        eventPublisher.publishEvent(new AfterDeleteEvent(relatedPublication));              
      });
    }
  }
  
  /**
   * Enqueue deletion of related publication search document 
   * when the related publication is deleted.
   * 
   * @param relatedPublication the deleted related publication.
   */
  @HandleAfterDelete
  public void onRelatedPublicationDeleted(RelatedPublication relatedPublication) {
    elasticsearchUpdateQueueService.enqueue(
        relatedPublication.getId(), 
        ElasticsearchType.related_publications, 
        ElasticsearchUpdateQueueAction.DELETE);
  }
  
  /**
   * Enqueue update of related publication search document when the related publication is updated.
   * 
   * @param relatedPublication the updated or related publication dataSet.
   */
  @HandleAfterCreate
  @HandleAfterSave
  public void onRelatedPublicationSaved(RelatedPublication relatedPublication) {
    elasticsearchUpdateQueueService.enqueue(
        relatedPublication.getId(), 
        ElasticsearchType.related_publications, 
        ElasticsearchUpdateQueueAction.UPSERT);
  }
  
  /**
   * Enqueue update of related publication search documents when the study changed.
   * 
   * @param study the updated, created or deleted study.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  @Async
  public void onStudyChanged(Study study) {
    enqueueUpserts(relatedPublicationRepository
        .streamIdsByStudyIdsContaining(study.getId()));
  }
  
  /**
   * Enqueue update of related publication search documents when the question changed.
   * 
   * @param question the updated, created or deleted question.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  @Async
  public void onQuestionChanged(Question question) {
    enqueueUpserts(relatedPublicationRepository
        .streamIdsByQuestionIdsContaining(question.getId()));
  }
  
  /**
   * Enqueue update of related publication search documents when the instrument changed.
   * 
   * @param instrument the updated, created or deleted instrument.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  @Async
  public void onInstrumentChanged(Instrument instrument) {
    enqueueUpserts(relatedPublicationRepository
        .streamIdsByInstrumentIdsContaining(instrument.getId()));
  }
  
  /**
   * Enqueue update of related publication search documents when the survey changed.
   * 
   * @param survey the updated, created or deleted survey.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  @Async
  public void onSurveyChanged(Survey survey) {
    enqueueUpserts(relatedPublicationRepository
        .streamIdsBySurveyIdsContaining(survey.getId()));
  }
  
  /**
   * Enqueue update of related publication search documents when the data set changed.
   * 
   * @param dataSet the updated, created or deleted data set.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  @Async
  public void onDataSetChanged(DataSet dataSet) {
    enqueueUpserts(relatedPublicationRepository
        .streamIdsByDataSetIdsContaining(dataSet.getId()));
  }
  
  /**
   * Enqueue update of related publication search documents when the variable changed.
   * 
   * @param variable the updated, created or deleted variable.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  @Async
  public void onVariableChanged(Variable variable) {
    enqueueUpserts(relatedPublicationRepository
        .streamIdsByVariableIdsContaining(variable.getId()));
  }
  
  private void enqueueUpserts(Stream<IdAndVersionProjection> relatedPublications) {
    try (Stream<IdAndVersionProjection> relatedPublicationStream = relatedPublications) {
      relatedPublicationStream.forEach(relatedPublication -> {
        elasticsearchUpdateQueueService.enqueue(relatedPublication.getId(),
            ElasticsearchType.related_publications, ElasticsearchUpdateQueueAction.UPSERT);
      });      
    }
  }
}

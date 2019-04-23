package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.service;

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
import org.springframework.data.rest.core.event.AfterSaveEvent;
import org.springframework.data.rest.core.event.BeforeSaveEvent;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.repository.RelatedPublicationRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.ElasticsearchUpdateQueueAction;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.studymanagement.repository.StudyRepository;
import eu.dzhw.fdz.metadatamanagement.studymanagement.service.StudyChangesProvider;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;

/**
 * This service for {@link RelatedPublicationService} will wait for delete events of a
 * {@link RelatedPublication}.
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
  private RelatedPublicationChangesProvider relatedPublicationChangesProvider;

  @Autowired
  private StudyChangesProvider studyChangesProvider;

  @Autowired
  private StudyRepository studyRepository;

  @Autowired
  private ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;

  @Autowired
  private ApplicationEventPublisher eventPublisher;

  /**
   * Enqueue deletion of related publication search document when the related publication is
   * deleted.
   * 
   * @param relatedPublication the deleted related publication.
   */
  @HandleAfterDelete
  public void onRelatedPublicationDeleted(RelatedPublication relatedPublication) {
    elasticsearchUpdateQueueService.enqueue(relatedPublication.getId(),
        ElasticsearchType.related_publications, ElasticsearchUpdateQueueAction.DELETE);
  }

  /**
   * Enqueue update of related publication search document when the related publication is updated.
   * 
   * @param relatedPublication the updated or related publication dataSet.
   */
  @HandleAfterCreate
  @HandleAfterSave
  public void onRelatedPublicationSaved(RelatedPublication relatedPublication) {
    elasticsearchUpdateQueueService.enqueue(relatedPublication.getId(),
        ElasticsearchType.related_publications, ElasticsearchUpdateQueueAction.UPSERT);
  }

  /**
   * Remember the old and new related publication.
   * 
   * @param relatedPublication the new related publication
   */
  @HandleBeforeSave
  public void onBeforeRelatedPublicationSaved(RelatedPublication relatedPublication) {
    relatedPublicationChangesProvider.put(relatedPublication,
        relatedPublicationRepository.findById(relatedPublication.getId()).orElse(null));
  }

  @HandleBeforeCreate
  public void onBeforeRelatedPublicationCreated(RelatedPublication relatedPublication) {
    relatedPublicationChangesProvider.put(relatedPublication, null);
  }

  @HandleBeforeDelete
  public void onBeforeRelatedPublicationDeleted(RelatedPublication relatedPublication) {
    relatedPublicationChangesProvider.put(null, relatedPublication);
  }

  /**
   * Enqueue update of related publication search documents when the study changed.
   * 
   * @param study the updated, created or deleted study.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onStudyChanged(Study study) {
    if (studyChangesProvider.hasStudySeriesChanged(study.getId())) {
      I18nString oldStudySeries = studyChangesProvider.getPreviousStudySeries(study.getId());
      // check if old study series does not exist anymore
      if (!studyRepository.existsByStudySeries(oldStudySeries)) {
        // update all related publications to new study series
        try (Stream<RelatedPublication> stream =
            relatedPublicationRepository.streamByStudySeriesesContaining(oldStudySeries)) {
          stream.forEach(publication -> {
            publication.getStudySerieses().remove(oldStudySeries);
            publication.getStudySerieses().add(study.getStudySeries());
            // emit before save and after save events
            eventPublisher.publishEvent(new BeforeSaveEvent(publication));
            relatedPublicationRepository.save(publication);
            eventPublisher.publishEvent(new AfterSaveEvent(publication));
          });
        }
      }
    }
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> relatedPublicationRepository.streamIdsByStudyIdsContaining(study.getId()),
        ElasticsearchType.related_publications);
  }


  /**
   * Enqueue update of related publication search documents when the question changed.
   * 
   * @param question the updated, created or deleted question.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onQuestionChanged(Question question) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> relatedPublicationRepository.streamIdsByQuestionIdsContaining(question.getId()),
        ElasticsearchType.related_publications);
  }

  /**
   * Enqueue update of related publication search documents when the instrument changed.
   * 
   * @param instrument the updated, created or deleted instrument.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onInstrumentChanged(Instrument instrument) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> relatedPublicationRepository.streamIdsByInstrumentIdsContaining(instrument.getId()),
        ElasticsearchType.related_publications);
  }

  /**
   * Enqueue update of related publication search documents when the survey changed.
   * 
   * @param survey the updated, created or deleted survey.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onSurveyChanged(Survey survey) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> relatedPublicationRepository.streamIdsBySurveyIdsContaining(survey.getId()),
        ElasticsearchType.related_publications);
  }

  /**
   * Enqueue update of related publication search documents when the data set changed.
   * 
   * @param dataSet the updated, created or deleted data set.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onDataSetChanged(DataSet dataSet) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> relatedPublicationRepository.streamIdsByDataSetIdsContaining(dataSet.getId()),
        ElasticsearchType.related_publications);
  }

  /**
   * Enqueue update of related publication search documents when the variable changed.
   * 
   * @param variable the updated, created or deleted variable.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onVariableChanged(Variable variable) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> relatedPublicationRepository.streamIdsByVariableIdsContaining(variable.getId()),
        ElasticsearchType.related_publications);
  }

  /**
   * Remove the given studyId from all publications.
   * 
   * @param studyId the id to be removed.
   */
  public void removeAllPublicationsFromStudy(String studyId) {
    try (Stream<RelatedPublication> publications =
        relatedPublicationRepository.streamByStudyIdsContaining(studyId)) {
      publications.forEach(publication -> {
        eventPublisher.publishEvent(new BeforeSaveEvent(publication));
        publication.getStudyIds().remove(studyId);
        relatedPublicationRepository.save(publication);
        eventPublisher.publishEvent(new AfterSaveEvent(publication));
      });
    }
  }

  /**
   * Assign the study to the given publication.
   * 
   * @param studyId An id of a {@link Study}.
   * @param publicationId An id of a {@link RelatedPublication}.
   */
  public void assignPublicationToStudy(String studyId, String publicationId) {
    relatedPublicationRepository.findById(publicationId).ifPresent(publication -> {
      publication.getStudyIds().add(studyId);
      eventPublisher.publishEvent(new BeforeSaveEvent(publication));
      relatedPublicationRepository.save(publication);
      eventPublisher.publishEvent(new AfterSaveEvent(publication));
    });
  }
  
  /**
   * Remove the study from the given publication.
   * 
   * @param studyId An id of a {@link Study}.
   * @param publicationId An id of a {@link RelatedPublication}.
   */
  public void removePublicationFromStudy(String studyId, String publicationId) {
    relatedPublicationRepository.findById(publicationId).ifPresent(publication -> {
      publication.getStudyIds().remove(studyId);
      eventPublisher.publishEvent(new BeforeSaveEvent(publication));
      relatedPublicationRepository.save(publication);
      eventPublisher.publishEvent(new AfterSaveEvent(publication));
    });
  }
}

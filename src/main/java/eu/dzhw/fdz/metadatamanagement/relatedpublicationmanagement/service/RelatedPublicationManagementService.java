package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.service;

import java.util.Optional;
import java.util.stream.Stream;

import org.javers.common.collections.Lists;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.service.CrudService;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.repository.RelatedPublicationRepository;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.service.helper.RelatedPublicationCrudHelper;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.studymanagement.repository.StudyRepository;
import eu.dzhw.fdz.metadatamanagement.studymanagement.service.StudyChangesProvider;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import lombok.RequiredArgsConstructor;

/**
 * Service for managing the domain object/aggregate {@link RelatedPublication}.
 * 
 * @author René Reitmann
 */
@Service
@RepositoryEventHandler
@RequiredArgsConstructor
public class RelatedPublicationManagementService implements CrudService<RelatedPublication> {

  private final RelatedPublicationRepository relatedPublicationRepository;

  private final StudyChangesProvider studyChangesProvider;

  private final StudyRepository studyRepository;

  private final ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;

  private final RelatedPublicationCrudHelper crudHelper;

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
            crudHelper.save(publication);
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
        if (publication.getStudyIds() != null) {
          publication.getStudyIds().remove(studyId);
        }
        crudHelper.save(publication);
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
      if (publication.getStudyIds() != null) {
        publication.getStudyIds().add(studyId);
      } else {
        publication.setStudyIds(Lists.immutableListOf(studyId));
      }
      crudHelper.save(publication);
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
      if (publication.getStudyIds() != null) {
        publication.getStudyIds().remove(studyId);
      }
      crudHelper.save(publication);
    });
  }

  @Override
  public Optional<RelatedPublication> read(String id) {
    return crudHelper.read(id);
  }

  @Override
  @Secured(value = {AuthoritiesConstants.PUBLISHER})
  public void delete(RelatedPublication publication) {
    crudHelper.delete(publication);
  }

  @Override
  @Secured(value = {AuthoritiesConstants.PUBLISHER})
  public RelatedPublication save(RelatedPublication publication) {
    return crudHelper.save(publication);
  }

  @Override
  @Secured(value = {AuthoritiesConstants.PUBLISHER})
  public RelatedPublication create(RelatedPublication publication) {
    return crudHelper.create(publication);
  }
}

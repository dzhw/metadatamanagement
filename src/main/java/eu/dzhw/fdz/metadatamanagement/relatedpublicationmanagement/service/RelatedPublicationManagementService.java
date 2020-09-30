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
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.repository.DataPackageRepository;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.service.DataPackageChangesProvider;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.repository.RelatedPublicationRepository;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.service.helper.RelatedPublicationCrudHelper;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
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

  private final DataPackageChangesProvider dataPackageChangesProvider;

  private final DataPackageRepository dataPackageRepository;

  private final ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;

  private final RelatedPublicationCrudHelper crudHelper;

  /**
   * Enqueue update of related publication search documents when the dataPackage changed.
   * 
   * @param dataPackage the updated, created or deleted dataPackage.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onDataPackageChanged(DataPackage dataPackage) {
    if (dataPackageChangesProvider.hasStudySeriesChanged(dataPackage.getId())) {
      I18nString oldStudySeries =
          dataPackageChangesProvider.getPreviousStudySeries(dataPackage.getId());
      // check if old dataPackage series does not exist anymore
      if (!dataPackageRepository.existsByStudySeries(oldStudySeries)) {
        // update all related publications to new dataPackage series
        try (Stream<RelatedPublication> stream =
            relatedPublicationRepository.streamByStudySeriesesContaining(oldStudySeries)) {
          stream.forEach(publication -> {
            publication.getStudySerieses().remove(oldStudySeries);
            publication.getStudySerieses().add(dataPackage.getStudySeries());
            // emit before save and after save events
            crudHelper.save(publication);
          });
        }
      }
    }
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> relatedPublicationRepository.streamIdsByDataPackageIdsContaining(dataPackage.getId()),
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
   * Remove the given dataPackageId from all publications.
   * 
   * @param dataPackageId the id to be removed.
   */
  public void removeAllPublicationsFromDataPackage(String dataPackageId) {
    try (Stream<RelatedPublication> publications =
        relatedPublicationRepository.streamByDataPackageIdsContaining(dataPackageId)) {
      publications.forEach(publication -> {
        if (publication.getDataPackageIds() != null) {
          publication.getDataPackageIds().remove(dataPackageId);
        }
        crudHelper.save(publication);
      });
    }
  }

  /**
   * Assign the dataPackage to the given publication.
   * 
   * @param dataPackageId An id of a {@link DataPackage}.
   * @param publicationId An id of a {@link RelatedPublication}.
   */
  public void assignPublicationToDataPackage(String dataPackageId, String publicationId) {
    relatedPublicationRepository.findById(publicationId).ifPresent(publication -> {
      if (publication.getDataPackageIds() != null) {
        if (publication.getDataPackageIds().contains(dataPackageId)) {
          return;
        }
        publication.getDataPackageIds().add(dataPackageId);
      } else {
        publication.setDataPackageIds(Lists.immutableListOf(dataPackageId));
      }
      crudHelper.save(publication);
    });
  }

  /**
   * Remove the dataPackage from the given publication.
   * 
   * @param dataPackageId An id of a {@link DataPackage}.
   * @param publicationId An id of a {@link RelatedPublication}.
   */
  public void removePublicationFromDataPackage(String dataPackageId, String publicationId) {
    relatedPublicationRepository.findById(publicationId).ifPresent(publication -> {
      if (publication.getDataPackageIds() != null) {
        publication.getDataPackageIds().remove(dataPackageId);
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


  @Override
  public Optional<RelatedPublication> readSearchDocument(String id) {
    return crudHelper.readSearchDocument(id);
  }
}

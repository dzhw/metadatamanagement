package eu.dzhw.fdz.metadatamanagement.common.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.rest.core.event.AfterCreateEvent;
import org.springframework.data.rest.core.event.AfterDeleteEvent;
import org.springframework.data.rest.core.event.AfterSaveEvent;
import org.springframework.data.rest.core.event.BeforeCreateEvent;
import org.springframework.data.rest.core.event.BeforeDeleteEvent;
import org.springframework.data.rest.core.event.BeforeSaveEvent;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.repository.BaseRepository;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.repository.ConceptRepository;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository.InstrumentRepository;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.repository.RelatedPublicationRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.ElasticsearchUpdateQueueAction;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.studymanagement.repository.StudyRepository;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.repository.VariableRepository;

/**
 * Component which implements CRUD functions for all {@link AbstractRdcDomainObject}s.
 *
 * @param <S> The {@link BaseRepository} managing the data access.
 * @param <T> The {@link AbstractRdcDomainObject}.
 * 
 * @author René Reitmann
 */
public class GenericDomainObjectCrudHelper<T extends AbstractRdcDomainObject, S extends BaseRepository<T, String>> {

  private final S repository;

  private final ApplicationEventPublisher applicationEventPublisher;

  private final ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;

  private static final List<String> defaultIgnoreProperties =
      Collections.unmodifiableList(Arrays.asList("createdDate", "createdBy", "version"));

  private final ElasticsearchType elasticsearchType;
  
  private final DomainObjectChangesProvider<T> domainObjectChangesProvider;

  /**
   * Create the CRUD service.
   *
   * @param repository The repository managing the data access for the domain objects.
   */
  public GenericDomainObjectCrudHelper(S repository,
      ApplicationEventPublisher applicationEventPublisher,
      ElasticsearchUpdateQueueService elasticsearchUpdateQueueService,
      DomainObjectChangesProvider<T> domainObjectChangesProvider) {
    this.repository = repository;
    this.applicationEventPublisher = applicationEventPublisher;
    this.elasticsearchUpdateQueueService = elasticsearchUpdateQueueService;
    this.elasticsearchType = computeElasticsearchType(repository);
    this.domainObjectChangesProvider = domainObjectChangesProvider;
  }

  private ElasticsearchType computeElasticsearchType(S repository) {
    if (StudyRepository.class.isAssignableFrom(repository.getClass())) {
      return ElasticsearchType.studies;
    }
    if (SurveyRepository.class.isAssignableFrom(repository.getClass())) {
      return ElasticsearchType.surveys;
    }
    if (DataSetRepository.class.isAssignableFrom(repository.getClass())) {
      return ElasticsearchType.data_sets;
    }
    if (VariableRepository.class.isAssignableFrom(repository.getClass())) {
      return ElasticsearchType.variables;
    }
    if (InstrumentRepository.class.isAssignableFrom(repository.getClass())) {
      return ElasticsearchType.instruments;
    }
    if (QuestionRepository.class.isAssignableFrom(repository.getClass())) {
      return ElasticsearchType.questions;
    }
    if (RelatedPublicationRepository.class.isAssignableFrom(repository.getClass())) {
      return ElasticsearchType.related_publications;
    }
    if (ConceptRepository.class.isAssignableFrom(repository.getClass())) {
      return ElasticsearchType.concepts;
    }
    return null;
  }

  /**
   * Create the given {@link AbstractRdcDomainObject}. Updates elasticsearch as well.
   * 
   * @param domainObject The {@link AbstractRdcDomainObject} to be created.
   * @return The created {@link AbstractRdcDomainObject}.
   */
  public T create(T domainObject) {
    return doCreate(domainObject);
  }

  private T doCreate(T domainObject) {
    if (domainObjectChangesProvider != null) {
      domainObjectChangesProvider.put(null, domainObject);
    }
    applicationEventPublisher.publishEvent(new BeforeCreateEvent(domainObject));    
    // insert is not captured by javers!
    T persisted = repository.save(domainObject);
    if (elasticsearchType != null) {
      elasticsearchUpdateQueueService.enqueue(persisted.getId(), this.elasticsearchType,
          ElasticsearchUpdateQueueAction.UPSERT);
      // flush the current changes
      elasticsearchUpdateQueueService.processQueueItems(this.elasticsearchType);
    }
    applicationEventPublisher.publishEvent(new AfterCreateEvent(persisted));
    return persisted;
  }

  /**
   * Save (update or create) the given {@link AbstractRdcDomainObject}. Updates elasticsearch as
   * well.
   * 
   * @param domainObject The {@link AbstractRdcDomainObject} to be saved.
   * 
   * @return The saved {@link AbstractRdcDomainObject}.
   */
  public T save(T domainObject) {
    T toBeSaved = domainObject;
    Optional<T> optional = repository.findById(domainObject.getId());
    if (optional.isEmpty()) {
      return doCreate(domainObject);
    }
    toBeSaved = optional.get();
    if (domainObjectChangesProvider != null) {
      domainObjectChangesProvider.put(toBeSaved, domainObject);
    }
    BeanUtils.copyProperties(domainObject, toBeSaved,
        defaultIgnoreProperties.toArray(new String[defaultIgnoreProperties.size()]));
    applicationEventPublisher.publishEvent(new BeforeSaveEvent(toBeSaved));
    T persisted = repository.save(toBeSaved);
    if (elasticsearchType != null) {
      elasticsearchUpdateQueueService.enqueue(persisted.getId(), this.elasticsearchType,
          ElasticsearchUpdateQueueAction.UPSERT);
      // flush the current changes
      elasticsearchUpdateQueueService.processQueueItems(this.elasticsearchType);
    }
    applicationEventPublisher.publishEvent(new AfterSaveEvent(persisted));
    return persisted;
  }

  /**
   * Delete the given {@link AbstractRdcDomainObject}. Updates elasticsearch as well.
   * 
   * @param domainObject The {@link AbstractRdcDomainObject} to be deleted.
   */
  public void delete(T domainObject) {
    if (domainObjectChangesProvider != null) {
      domainObjectChangesProvider.put(domainObject, null);
    }
    applicationEventPublisher.publishEvent(new BeforeDeleteEvent(domainObject));
    repository.delete(domainObject);
    if (elasticsearchType != null) {
      elasticsearchUpdateQueueService.enqueue(domainObject.getId(), this.elasticsearchType,
          ElasticsearchUpdateQueueAction.DELETE);
      // flush the current changes
      elasticsearchUpdateQueueService.processQueueItems(this.elasticsearchType);
    }
    applicationEventPublisher.publishEvent(new AfterDeleteEvent(domainObject));
  }

  /**
   * Find the {@link AbstractRdcDomainObject} by the given id.
   * 
   * @param id The id of the domain object.
   * @return An optional domain object.
   */
  public Optional<T> read(String id) {
    return repository.findById(id);
  }
}

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
import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.ElasticsearchUpdateQueueAction;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;

/**
 * Service which implements CRUD functions for all {@link AbstractRdcDomainObject}s.
 *
 * @param <S> The {@link BaseRepository} managing the data access.
 * @param <T> The {@link AbstractRdcDomainObject}.
 * 
 * @author René Reitmann
 */
public class GenericDomainObjectCrudHelper
  <T extends AbstractRdcDomainObject, S extends BaseRepository<T, String>> {

  private final S repository;

  private final ApplicationEventPublisher applicationEventPublisher;

  private final ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;

  private static final List<String> defaultIgnoreProperties =
      Collections.unmodifiableList(Arrays.asList("createdDate", "createdBy", "version"));

  private final ElasticsearchType elasticsearchType;

  /**
   * Create the CRUD service.
   *
   * @param repository The repository managing the data access for the domain objects.
   */
  public GenericDomainObjectCrudHelper(S repository,
      ApplicationEventPublisher applicationEventPublisher,
      ElasticsearchUpdateQueueService elasticsearchUpdateQueueService,
      ElasticsearchType elasticsearchType) {
    this.repository = repository;
    this.applicationEventPublisher = applicationEventPublisher;
    this.elasticsearchUpdateQueueService = elasticsearchUpdateQueueService;
    this.elasticsearchType = elasticsearchType;
  }

  /**
   * Create the given {@link AbstractRdcDomainObject}. Updates elasticsearch as well.
   * 
   * @param domainObject The {@link AbstractRdcDomainObject} to be created.
   * @param forceElasticsearchUpdate true if the search index shall be updated immediately.
   * @return The created {@link AbstractRdcDomainObject}.
   */
  public T create(T domainObject, boolean forceElasticsearchUpdate) {
    return doCreate(domainObject, forceElasticsearchUpdate);
  }
  
  private T doCreate(T domainObject, boolean forceElasticsearchUpdate) {
    applicationEventPublisher.publishEvent(new BeforeCreateEvent(domainObject));
    // insert is not captured by javers!
    T persisted = repository.save(domainObject);
    if (elasticsearchUpdateQueueService != null && forceElasticsearchUpdate) {      
      elasticsearchUpdateQueueService.enqueue(persisted.getId(), this.elasticsearchType,
          ElasticsearchUpdateQueueAction.UPSERT);
      if (forceElasticsearchUpdate) {        
        // flush the current changes
        elasticsearchUpdateQueueService.processQueueItems(this.elasticsearchType);
      }
    }
    applicationEventPublisher.publishEvent(new AfterCreateEvent(persisted));
    return persisted;
  }

  /**
   * Save (update or create) the given {@link AbstractRdcDomainObject}. Updates elasticsearch as
   * well.
   * 
   * @param domainObject The {@link AbstractRdcDomainObject} to be saved.
   * @param forceElasticsearchUpdate true if the search index shall be updated immediately.
   * @return The saved {@link AbstractRdcDomainObject}.
   */
  public T save(T domainObject, boolean forceElasticsearchUpdate) {
    T toBeSaved = domainObject;
    Optional<T> optional = repository.findById(domainObject.getId());
    if (optional.isEmpty()) {
      return doCreate(domainObject, forceElasticsearchUpdate);
    }
    toBeSaved = optional.get();
    BeanUtils.copyProperties(domainObject, toBeSaved,
        defaultIgnoreProperties.toArray(new String[defaultIgnoreProperties.size()]));
    applicationEventPublisher.publishEvent(new BeforeSaveEvent(toBeSaved));
    T persisted = repository.save(toBeSaved);
    if (elasticsearchUpdateQueueService != null) {      
      elasticsearchUpdateQueueService.enqueue(persisted.getId(), this.elasticsearchType,
          ElasticsearchUpdateQueueAction.UPSERT);
      if (forceElasticsearchUpdate) {        
        // flush the current changes
        elasticsearchUpdateQueueService.processQueueItems(this.elasticsearchType);
      }
    }
    applicationEventPublisher.publishEvent(new AfterSaveEvent(persisted));
    return persisted;
  }

  /**
   * Delete the given {@link AbstractRdcDomainObject}. Updates elasticsearch as well.
   * 
   * @param domainObject The {@link AbstractRdcDomainObject} to be deleted.
   * @param forceElasticsearchUpdate true if the search index shall be updated immediately.
   */
  public void delete(T domainObject, boolean forceElasticsearchUpdate) {
    applicationEventPublisher.publishEvent(new BeforeDeleteEvent(domainObject));
    repository.delete(domainObject);
    if (elasticsearchUpdateQueueService != null) {      
      elasticsearchUpdateQueueService.enqueue(domainObject.getId(), this.elasticsearchType,
          ElasticsearchUpdateQueueAction.DELETE);
      if (forceElasticsearchUpdate) {        
        // flush the current changes
        elasticsearchUpdateQueueService.processQueueItems(this.elasticsearchType);
      }
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

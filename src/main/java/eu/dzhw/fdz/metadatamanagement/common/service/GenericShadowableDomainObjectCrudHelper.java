package eu.dzhw.fdz.metadatamanagement.common.service;

import org.springframework.context.ApplicationEventPublisher;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractShadowableRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.ShadowCopyCreateNotAllowedException;
import eu.dzhw.fdz.metadatamanagement.common.domain.ShadowCopyDeleteNotAllowedException;
import eu.dzhw.fdz.metadatamanagement.common.domain.ShadowCopySaveNotAllowedException;
import eu.dzhw.fdz.metadatamanagement.common.repository.BaseRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.ElasticsearchUpdateQueueAction;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;

/**
 * CRUD Service Helper for {@link AbstractShadowableRdcDomainObject}.
 * 
 * @param <S> The {@link BaseRepository} managing the data access.
 * @param <T> The {@link AbstractShadowableRdcDomainObject}.
 * 
 * @author René Reitmann
 */
public class GenericShadowableDomainObjectCrudHelper
    <T extends AbstractShadowableRdcDomainObject, S extends BaseRepository<T, String>>
    extends GenericDomainObjectCrudHelper<T, S> {

  public GenericShadowableDomainObjectCrudHelper(S repository,
      ApplicationEventPublisher applicationEventPublisher,
      ElasticsearchUpdateQueueService elasticsearchUpdateQueueService,
      DomainObjectChangesProvider<T> domainObjectChangesProvider) {
    super(repository, applicationEventPublisher, elasticsearchUpdateQueueService,
        domainObjectChangesProvider);
  }

  /**
   * Create the given master {@link AbstractShadowableRdcDomainObject}. Updates elasticsearch as
   * well.
   * 
   * @param domainObject The master {@link AbstractShadowableRdcDomainObject} to be created.
   * @return The created master {@link AbstractShadowableRdcDomainObject}.
   */
  public T createMaster(T domainObject) {
    if (domainObject.isShadow()) {
      throw new ShadowCopyCreateNotAllowedException();
    }
    return super.create(domainObject);
  }

  /**
   * Save (update or create) the given master {@link AbstractShadowableRdcDomainObject}. Updates
   * elasticsearch as well.
   * 
   * @param domainObject The master {@link AbstractShadowableRdcDomainObject} to be saved.
   * @return The saved master {@link AbstractShadowableRdcDomainObject}.
   */
  public T saveMaster(T domainObject) {
    if (domainObject.isShadow()) {
      throw new ShadowCopySaveNotAllowedException();
    }
    return super.save(domainObject);
  }

  /**
   * Delete the given master {@link AbstractShadowableRdcDomainObject}. Updates elasticsearch as
   * well.
   * 
   * @param domainObject The master {@link AbstractShadowableRdcDomainObject} to be deleted.
   */
  public void deleteMaster(T domainObject) {
    if (domainObject.isShadow()) {
      throw new ShadowCopyDeleteNotAllowedException();
    }
    super.delete(domainObject);
  }

  /**
   * Create the given shadow {@link AbstractShadowableRdcDomainObject}. Does not do any
   * elasticsearch updates at all. Cause the entire shadow project needs to be reindexed anyway.
   * 
   * @param domainObject The shadow {@link AbstractShadowableRdcDomainObject} to be created.
   * @return The created shadow {@link AbstractShadowableRdcDomainObject}.
   */
  public T createShadow(T domainObject) {
    if (!domainObject.isShadow()) {
      throw new IllegalArgumentException("Expected a shadow copy for creating.");
    }
    return repository.save(domainObject);
  }

  /**
   * Save (update or create) the given shadow {@link AbstractShadowableRdcDomainObject}. Does not do
   * any elasticsearch updates at all. Cause the entire shadow project needs to be reindex anyway.
   * 
   * @param domainObject The shadow {@link AbstractShadowableRdcDomainObject} to be saved.
   * @return The saved shadow {@link AbstractShadowableRdcDomainObject}.
   */
  public T saveShadow(T domainObject) {
    if (!domainObject.isShadow()) {
      throw new IllegalArgumentException("Expected a shadow copy for saving.");
    }
    return repository.save(domainObject);
  }

  /**
   * Delete the given shadow {@link AbstractShadowableRdcDomainObject}. Updates elasticsearch as
   * well. But does not throw any events cause the request scoped
   * {@link DomainObjectChangesProvider} do not work during shadow copying.
   * 
   * @param domainObject The master {@link AbstractShadowableRdcDomainObject} to be deleted.
   */
  public void deleteShadow(T domainObject) {
    if (!domainObject.isShadow()) {
      throw new IllegalArgumentException("Expected a shadow copy for deleting.");
    }
    repository.delete(domainObject);
    if (elasticsearchType != null) {
      elasticsearchUpdateQueueService.enqueue(domainObject.getId(), this.elasticsearchType,
          ElasticsearchUpdateQueueAction.DELETE);
      // flush the current changes
      elasticsearchUpdateQueueService.processQueueItems(this.elasticsearchType);
    }
  }

  @Override
  public T create(T domainObject) {
    throw new IllegalAccessError(
        "Services of shadowable domain objects must not call this directly");
  }

  @Override
  public T save(T domainObject) {
    throw new IllegalAccessError(
        "Services of shadowable domain objects must not call this directly");
  }

  @Override
  public void delete(T domainObject) {
    throw new IllegalAccessError(
        "Services of shadowable domain objects must not call this directly");
  }
}

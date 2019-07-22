package eu.dzhw.fdz.metadatamanagement.common.service;

import org.springframework.context.ApplicationEventPublisher;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractShadowableRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.ShadowCopyCreateNotAllowedException;
import eu.dzhw.fdz.metadatamanagement.common.domain.ShadowCopyDeleteNotAllowedException;
import eu.dzhw.fdz.metadatamanagement.common.domain.ShadowCopySaveNotAllowedException;
import eu.dzhw.fdz.metadatamanagement.common.repository.BaseRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
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
      ElasticsearchType elasticsearchType) {
    super(repository, applicationEventPublisher, elasticsearchUpdateQueueService,
        elasticsearchType);
  }

  public GenericShadowableDomainObjectCrudHelper(S repository,
      ApplicationEventPublisher applicationEventPublisher) {
    super(repository, applicationEventPublisher, null,
        null);
  }

  /**
   * Create the given master {@link AbstractShadowableRdcDomainObject}. Updates elasticsearch as
   * well.
   * 
   * @param domainObject The master {@link AbstractShadowableRdcDomainObject} to be created.
   * @param forceElasticsearchUpdate true if the search index shall be updated immediately.
   * @return The created master {@link AbstractShadowableRdcDomainObject}.
   */
  public T createMaster(T domainObject, boolean forceElasticsearchUpdate) {
    if (domainObject.isShadow()) {
      throw new ShadowCopyCreateNotAllowedException();
    }
    return super.create(domainObject, forceElasticsearchUpdate);
  }

  /**
   * Save (update or create) the given master {@link AbstractShadowableRdcDomainObject}. Updates
   * elasticsearch as well.
   * 
   * @param domainObject The master {@link AbstractShadowableRdcDomainObject} to be saved.
   * @param forceElasticsearchUpdate true if the search index shall be updated immediately.
   * @return The saved master {@link AbstractShadowableRdcDomainObject}.
   */
  public T saveMaster(T domainObject, boolean forceElasticsearchUpdate) {
    if (domainObject.isShadow()) {
      throw new ShadowCopySaveNotAllowedException();
    }
    return super.save(domainObject, forceElasticsearchUpdate);
  }

  /**
   * Delete the given master {@link AbstractShadowableRdcDomainObject}. Updates elasticsearch as
   * well.
   * 
   * @param domainObject The master {@link AbstractShadowableRdcDomainObject} to be deleted.
   * @param forceElasticsearchUpdate true if the search index shall be updated immediately.
   */
  public void deleteMaster(T domainObject, boolean forceElasticsearchUpdate) {
    if (domainObject.isShadow()) {
      throw new ShadowCopyDeleteNotAllowedException();
    }
    super.delete(domainObject, forceElasticsearchUpdate);
  }

  /**
   * Create the given shadow {@link AbstractShadowableRdcDomainObject}. Updates elasticsearch as
   * well.
   * 
   * @param domainObject The shadow {@link AbstractShadowableRdcDomainObject} to be created.
   * @param forceElasticsearchUpdate true if the search index shall be updated immediately.
   * @return The created shadow {@link AbstractShadowableRdcDomainObject}.
   */
  public T createShadow(T domainObject, boolean forceElasticsearchUpdate) {
    if (!domainObject.isShadow()) {
      throw new IllegalArgumentException("Expected a shadow copy for creating.");
    }
    return super.create(domainObject, forceElasticsearchUpdate);
  }

  /**
   * Save (update or create) the given shadow {@link AbstractShadowableRdcDomainObject}. Updates
   * elasticsearch as well.
   * 
   * @param domainObject The shadow {@link AbstractShadowableRdcDomainObject} to be saved.
   * @param forceElasticsearchUpdate true if the search index shall be updated immediately.
   * @return The saved shadow {@link AbstractShadowableRdcDomainObject}.
   */
  public T saveShadow(T domainObject, boolean forceElasticsearchUpdate) {
    if (!domainObject.isShadow()) {
      throw new IllegalArgumentException("Expected a shadow copy for saving.");
    }
    return super.save(domainObject, forceElasticsearchUpdate);
  }

  /**
   * Delete the given master {@link AbstractShadowableRdcDomainObject}. Updates elasticsearch as
   * well.
   * 
   * @param domainObject The master {@link AbstractShadowableRdcDomainObject} to be deleted.
   * @param forceElasticsearchUpdate true if the search index shall be updated immediately.
   */
  public void deleteShadow(T domainObject, boolean forceElasticsearchUpdate) {
    if (!domainObject.isShadow()) {
      throw new IllegalArgumentException("Expected a shadow copy for deleting.");
    }
    super.delete(domainObject, forceElasticsearchUpdate);
  }
  
  @Override
  public T create(T domainObject, boolean forceElasticsearchUpadte) {
    throw new IllegalAccessError("Services of shadowable domain objects must not call this directly");
  }

  @Override
  public T save(T domainObject, boolean forceElasticsearchUpdate) {
    throw new IllegalAccessError("Services of shadowable domain objects must not call this directly");
  }

  @Override
  public void delete(T domainObject, boolean forceElasticsearchUpdate) {
    throw new IllegalAccessError("Services of shadowable domain objects must not call this directly");
  }
}

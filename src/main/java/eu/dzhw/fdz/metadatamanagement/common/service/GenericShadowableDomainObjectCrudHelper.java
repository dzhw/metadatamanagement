package eu.dzhw.fdz.metadatamanagement.common.service;

import java.util.Optional;

import eu.dzhw.fdz.metadatamanagement.authmanagement.security.SecurityUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.google.gson.Gson;
import com.querydsl.core.types.Predicate;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractShadowableRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.QAbstractShadowableRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.ShadowCopyCreateNotAllowedException;
import eu.dzhw.fdz.metadatamanagement.common.domain.ShadowCopyDeleteNotAllowedException;
import eu.dzhw.fdz.metadatamanagement.common.domain.ShadowCopySaveNotAllowedException;
import eu.dzhw.fdz.metadatamanagement.common.repository.BaseRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.ExcludeFieldsHelper;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.SearchDocumentInterface;
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
public class GenericShadowableDomainObjectCrudHelper<T extends AbstractShadowableRdcDomainObject,
      S extends BaseRepository<T, String>>
    extends GenericDomainObjectCrudHelper<T, S> {

  private static final QAbstractShadowableRdcDomainObject shadowQuery =
      QAbstractShadowableRdcDomainObject.abstractShadowableRdcDomainObject;

  /**
   * Construct the helper.
   */
  public GenericShadowableDomainObjectCrudHelper(
      S repository,
      ApplicationEventPublisher applicationEventPublisher,
      ElasticsearchUpdateQueueService elasticsearchUpdateQueueService,
      DomainObjectChangesProvider<T> domainObjectChangesProvider,
      RestHighLevelClient elasticsearchClient,
      Class<? extends T> searchDocumentClass,
      Gson gson
  ) {
    super(repository, applicationEventPublisher, elasticsearchUpdateQueueService,
        domainObjectChangesProvider, elasticsearchClient, searchDocumentClass, gson);
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
   * any elasticsearch updates at all. Cause the entire shadow project needs to be reindexed anyway.
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
    repository.deleteById(domainObject.getId());
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

  /**
   * Find the {@link SearchDocumentInterface} which corresponds to the
   * {@link AbstractRdcDomainObject}. Public users (anonymous users) only get the latest shadow.
   *
   * @param id The id of the domain object.
   * @return An optional domain object.
   */
  @Override
  public Optional<T> readSearchDocument(String id) {
    if (elasticsearchType == null) {
      return Optional.empty();
    }
    if (!SecurityUtils.isUserAnonymous()) {
      return super.readSearchDocument(id);
    } else {
      Optional<T> searchDocument = null;
      if (id.matches("^.*-\\d+.\\d+.\\d+$")) {
        // explicit get by id containing version
        searchDocument = super.readSearchDocument(id);
      } else {
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.fetchSource(null,
            ExcludeFieldsHelper.getFieldsToExcludeOnDeserialization(searchDocumentClass));
        builder.query(QueryBuilders.constantScoreQuery(
            QueryBuilders.boolQuery().must(QueryBuilders.termQuery("shadow", true))
                .must(QueryBuilders.termQuery("masterId", id))
                .mustNot(QueryBuilders.existsQuery("successorId"))))
            .size(1);
        searchDocument = super.executeSearch(new SearchRequest().source(builder));
      }
      return searchDocument;
    }
  }

  /**
   * Find all shadows for the given masterId.
   *
   * @param masterId The id of the master.
   * @param pageable used for paging and sorting the results
   * @return A page containing all shadows. Can be empty.
   */
  public Page<T> findAllShadows(String masterId, Pageable pageable) {
    return repository.findAll(isShadowOf(masterId), pageable);
  }

  private Predicate isShadowOf(String masterId) {
    return shadowQuery.shadow.isTrue().and(shadowQuery.masterId.eq(masterId));
  }
}

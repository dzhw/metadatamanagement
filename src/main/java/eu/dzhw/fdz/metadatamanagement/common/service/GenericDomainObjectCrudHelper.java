package eu.dzhw.fdz.metadatamanagement.common.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.ConstantScoreQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.search.SourceConfig;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.rest.core.event.AfterCreateEvent;
import org.springframework.data.rest.core.event.AfterDeleteEvent;
import org.springframework.data.rest.core.event.AfterSaveEvent;
import org.springframework.data.rest.core.event.BeforeCreateEvent;
import org.springframework.data.rest.core.event.BeforeDeleteEvent;
import org.springframework.data.rest.core.event.BeforeSaveEvent;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.repository.AnalysisPackageRepository;
import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.repository.BaseRepository;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.repository.ConceptRepository;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository.InstrumentRepository;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.repository.RelatedPublicationRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.dao.exception.ElasticsearchIoException;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.ExcludeFieldsHelper;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.SearchDocumentInterface;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.ElasticsearchUpdateQueueAction;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.repository.DataPackageRepository;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.repository.VariableRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;

import static java.util.Arrays.asList;

/**
 * Component which implements CRUD functions for all {@link AbstractRdcDomainObject}s.
 *
 * @param <S> The {@link BaseRepository} managing the data access.
 * @param <T> The {@link AbstractRdcDomainObject}.
 *
 * @author René Reitmann
 */
public class GenericDomainObjectCrudHelper<T extends AbstractRdcDomainObject,
    S extends BaseRepository<T, String>> {

  protected final S repository;

  private final ApplicationEventPublisher applicationEventPublisher;

  protected final ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;

  private static final List<String> defaultIgnoreProperties =
      Collections.unmodifiableList(Arrays.asList("createdDate", "createdBy", "version"));

  protected final ElasticsearchType elasticsearchType;

  private final DomainObjectChangesProvider<T> domainObjectChangesProvider;

  protected final Class<? extends T> searchDocumentClass;

  protected final ElasticsearchClient elasticsearchClient;

  /**
   * Create the CRUD service.
   *
   * @param repository The repository managing the data access for the domain objects.
   */
  public GenericDomainObjectCrudHelper(S repository,
      ApplicationEventPublisher applicationEventPublisher,
      ElasticsearchUpdateQueueService elasticsearchUpdateQueueService,
      DomainObjectChangesProvider<T> domainObjectChangesProvider,
      ElasticsearchClient elasticsearchClient, Class<? extends T> searchDocumentClass)
  {
    this.repository = repository;
    this.applicationEventPublisher = applicationEventPublisher;
    this.elasticsearchUpdateQueueService = elasticsearchUpdateQueueService;
    this.elasticsearchType = computeElasticsearchType(repository);
    this.domainObjectChangesProvider = domainObjectChangesProvider;
    this.elasticsearchClient = elasticsearchClient;
    this.searchDocumentClass = searchDocumentClass;
  }

  private ElasticsearchType computeElasticsearchType(S repository) {
    if (DataPackageRepository.class.isAssignableFrom(repository.getClass())) {
      return ElasticsearchType.data_packages;
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
    if (AnalysisPackageRepository.class.isAssignableFrom(repository.getClass())) {
      return ElasticsearchType.analysis_packages;
    }
    if (DataAcquisitionProjectRepository.class.isAssignableFrom(repository.getClass())) {
      return ElasticsearchType.data_acquisition_projects;
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
    Optional<T> optional = repository.findById(domainObject.getId());
    if (optional.isEmpty()) {
      return doCreate(domainObject);
    }
    T toBeSaved = optional.get();
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

  /**
   * Find the {@link SearchDocumentInterface} which corresponds to the
   * {@link AbstractRdcDomainObject}.
   *
   * @param id The id of the domain object.
   * @return An optional domain object.
   */
  public Optional<T> readSearchDocument(String id) {
    if (elasticsearchType == null) {
      return Optional.empty();
    }
    // configure excluded fields
    final var excludeFields = asList(ExcludeFieldsHelper.getFieldsToExcludeOnDeserialization(searchDocumentClass));
    final var sourceConfig = SourceConfig.of(sc -> sc.filter(f -> f.excludes(excludeFields)));
    // configure query
    final var haveId = TermQuery.of(t -> t.field("id").value(id));
    final var constantScoreQuery = ConstantScoreQuery.of(csq -> csq.filter(q -> q
      .bool(b -> b
        .must(haveId)
      )
    ));
    // create the search request
    final var searchRequest = SearchRequest.of(s -> s
      .source(sourceConfig)
      .query(constantScoreQuery)
      .size(1)
    );
    return this.executeSearch(searchRequest);
  }

  @SuppressFBWarnings("BC_UNCONFIRMED_CAST_OF_RETURN_VALUE")
  protected Optional<T> executeSearch(SearchRequest request) {
    try {
      final var response = elasticsearchClient.search(request, searchDocumentClass);
      final var hits = response.hits().hits();
      if (hits.isEmpty()) {
        return Optional.empty();
      } else {
        T result = hits.get(0).source();
        return Optional.ofNullable(result);
      }
    } catch (IOException e) {
      throw new ElasticsearchIoException(e);
    }
  }
}

package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.service;

import java.io.IOException;
import java.util.List;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.search.Hit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.DataPackageSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.ExcludeFieldsHelper;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import lombok.RequiredArgsConstructor;

/**
 * Service to get released dataPackages out of elastic search.
 *
 * @author tgehrke
 *
 */
@Service
@RequiredArgsConstructor
public class DataPackageListService {

  private final ElasticsearchClient elasticsearchClient;

  /**
   * Request released dataPackages sort by title (DE) with a pagination defined by page and size.
   *
   * @param page the pagination page
   * @param size the count of items to query
   * @return a list of searched dataPackage documents wrapped in a page object.
   * @throws IOException if search failed
   */
  public Page<DataPackageSearchDocument> loadDataPackages(int page, int size) throws IOException {
    final var request = SearchRequest.of(r -> r
      .source(c -> c
        .filter(f -> f
          .excludes(List.of(ExcludeFieldsHelper.getFieldsToExcludeOnDeserialization(DataPackageSearchDocument.class)))))
      .query(q -> q
        .bool(b -> b
          .filter(f -> f.term(t -> t.field("shadow").value(true)))
          .mustNot(m -> m.exists(e -> e.field("successorId")))))
      .from(page * size)
      .size(size)
      .sort(s -> s.field(f -> f.field("title.de").order(SortOrder.Asc)))
    );
    return executeSearchAndCreatePage(page, size, request);
  }

  private Page<DataPackageSearchDocument> executeSearchAndCreatePage(int page, int size, SearchRequest searchRequest)
    throws IOException
  {
    final var response = elasticsearchClient.search(searchRequest, DataPackageSearchDocument.class);
    final var hits = response.hits().hits().parallelStream().map(Hit::source).toList();
    return new PageImpl<>(hits, PageRequest.of(page, size), hits.size());
  }

  /**
   * Get the data packages which shall be pinned to the start page. The page will contain the pinned
   * data packages sorted by release date, starting with the latest release.
   *
   * @param page the page number
   * @param size the number of data package per page
   * @return a list of pinned data package documents wrapped in a page object
   * @throws IOException if search failed
   */
  public Page<DataPackageSearchDocument> loadPinnedDataPackages(int page, int size)
      throws IOException
  {
    final var request = SearchRequest.of(r -> r
      .source(c -> c
        .filter(f -> f
          .excludes(List.of(ExcludeFieldsHelper.getFieldsToExcludeOnDeserialization(DataPackageSearchDocument.class)))))
      .index(ElasticsearchType.data_packages.name())
      .query(q -> q
        .bool(b -> b
          .filter(f -> f.term(t -> t.field("shadow").value(true)))
          .filter(f -> f.term(t -> t.field("release.pinToStartPage").value(true)))
          .mustNot(m -> m.exists(e -> e.field("successorId")))))
      .from(page * size)
      .size(size)
      .sort(s -> s.field(f -> f.field("release.lastDate").order(SortOrder.Desc)))
    );
    return executeSearchAndCreatePage(page, size, request);
  }
}

package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

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

  private final RestHighLevelClient elasticsearchClient;

  private final Gson gson;

  /**
   * Request released dataPackages sort by title (DE) with a pagination defined by page and size.
   * 
   * @param page the pagination page
   * @param size the count of items to query
   * @return a list of searched dataPackage documents wrapped in a page object.
   * @throws IOException if search failed
   */
  public Page<DataPackageSearchDocument> loadDataPackages(int page, int size) throws IOException {
    SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

    sourceBuilder.fetchSource(null,
        ExcludeFieldsHelper.getFieldsToExcludeOnDeserialization(DataPackageSearchDocument.class));
    sourceBuilder
        .query(QueryBuilders.boolQuery().filter(QueryBuilders.termQuery("shadow", true))
            .mustNot(QueryBuilders.existsQuery("successorId")))
        .from(page * size).size(size).sort("title.de", SortOrder.ASC);

    return executeSearchAndCreatePage(page, size, sourceBuilder);
  }

  private Page<DataPackageSearchDocument> executeSearchAndCreatePage(int page, int size,
      SearchSourceBuilder sourceBuilder) throws IOException {
    SearchResponse response = elasticsearchClient.search(
        new SearchRequest().source(sourceBuilder).indices(ElasticsearchType.data_packages.name()),
        RequestOptions.DEFAULT);

    SearchHit[] searchHits = response.getHits().getHits();
    List<DataPackageSearchDocument> hits = new ArrayList<>(searchHits.length);
    for (SearchHit searchHit : searchHits) {
      hits.add(gson.fromJson(searchHit.getSourceAsString(), DataPackageSearchDocument.class));
    }

    long total = response.getHits().getTotalHits().value;

    PageRequest pageRequest = PageRequest.of(page, size);
    Page<DataPackageSearchDocument> resultPage =
        new PageImpl<DataPackageSearchDocument>(hits, pageRequest, total);
    return resultPage;
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
      throws IOException {
    SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

    sourceBuilder.fetchSource(null,
        ExcludeFieldsHelper.getFieldsToExcludeOnDeserialization(DataPackageSearchDocument.class));
    sourceBuilder
        .query(QueryBuilders.boolQuery().filter(QueryBuilders.termQuery("shadow", true))
            .filter(QueryBuilders.termQuery("release.pinToStartPage", true))
            .mustNot(QueryBuilders.existsQuery("successorId")))
        .from(page * size).size(size).sort("release.lastDate", SortOrder.DESC);

    return executeSearchAndCreatePage(page, size, sourceBuilder);
  }
}

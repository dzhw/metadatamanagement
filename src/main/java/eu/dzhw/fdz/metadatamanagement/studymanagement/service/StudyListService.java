package eu.dzhw.fdz.metadatamanagement.studymanagement.service;

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

import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.ExcludeFieldsHelper;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.StudySearchDocument;
import lombok.RequiredArgsConstructor;

/**
 * Service to get released studies out of elastic search.
 * 
 * @author tgehrke
 *
 */
@Service
@RequiredArgsConstructor
public class StudyListService {

  private final RestHighLevelClient elasticsearchClient;

  private final Gson gson;

  /**
   * Request released studies sort by title (DE) with a pagination defined by page and size.
   * 
   * @param page the pagination page
   * @param size the count of items to query
   * @return a list of searched study documents wrapped in a page object.
   * @throws IOException if search failed
   */
  public Page<StudySearchDocument> loadStudies(int page, int size) throws IOException {
    SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

    sourceBuilder.fetchSource(null,
        ExcludeFieldsHelper.getFieldsToExcludeOnDeserialization(StudySearchDocument.class));
    sourceBuilder
        .query(QueryBuilders.boolQuery().filter(QueryBuilders.termQuery("shadow", true))
            .mustNot(QueryBuilders.existsQuery("successorId")))
        .from(page * size).size(size).sort("title.de", SortOrder.ASC);
    SearchResponse response = elasticsearchClient.search(
        new SearchRequest().source(sourceBuilder).indices("studies"), RequestOptions.DEFAULT);

    SearchHit[] searchHits = response.getHits().getHits();
    List<StudySearchDocument> hits = new ArrayList<>(searchHits.length);
    for (SearchHit searchHit : searchHits) {
      hits.add(gson.fromJson(searchHit.getSourceAsString(), StudySearchDocument.class));
    }

    long total = response.getHits().getTotalHits().value;

    PageRequest pageRequest = PageRequest.of(page, size);
    Page<StudySearchDocument> resultPage =
        new PageImpl<StudySearchDocument>(hits, pageRequest, total);

    return resultPage;
  }
}

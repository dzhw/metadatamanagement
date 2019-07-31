package eu.dzhw.fdz.metadatamanagement.studymanagement.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.ExcludeFieldsHelper;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.StudySearchDocument;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
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

  private final JestClient jestClient;

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
    Search search = new Search.Builder(sourceBuilder.toString()).addIndex("studies").build();
    SearchResult searchResult = jestClient.execute(search);
    List<StudySearchDocument> hits = searchResult.getHits(StudySearchDocument.class).stream()
        .map(hit -> hit.source).collect(Collectors.toList());
    long total = searchResult.getTotal();

    PageRequest pageRequest = PageRequest.of(page, size);
    Page<StudySearchDocument> resultPage =
        new PageImpl<StudySearchDocument>(hits, pageRequest, total);

    return resultPage;
  }
}

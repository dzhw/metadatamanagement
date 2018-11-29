package eu.dzhw.fdz.metadatamanagement.studymanagement.service;

import java.io.IOException;
import java.util.List;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;

import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.StudySearchDocument;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Search;
import lombok.extern.slf4j.Slf4j;
/**
 * Service to get released studies out of elastic search.
 * @author tgehrke
 *
 */
@Slf4j
@Service
public class StudyListService {
  @Autowired
  JestClient jestClient;

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
    sourceBuilder.query(QueryBuilders.boolQuery().filter(QueryBuilders.existsQuery("release")))
        .from(page * size).size(size).sort("title.de", SortOrder.ASC);
    Search search = new Search.Builder(sourceBuilder.toString()).addIndex("studies").build();
    JestResult searchResult = jestClient.execute(search);
    List<StudySearchDocument> hits = searchResult.getSourceAsObjectList(StudySearchDocument.class);
    long total;
    try {
      JsonObject jsonObject = searchResult.getJsonObject();
      String asString = jsonObject.get("hits").getAsJsonObject().get("total").getAsString();
      total = Long.parseLong(asString);
    } catch (Exception e) {
      log.warn("failed to get total number of elemets from search result", e);
      total = 0;
    }

    PageRequest pageRequest = PageRequest.of(page, size);
    Page<StudySearchDocument> resultPage =
        new PageImpl<StudySearchDocument>(hits, pageRequest, total);

    return resultPage;
  }
}

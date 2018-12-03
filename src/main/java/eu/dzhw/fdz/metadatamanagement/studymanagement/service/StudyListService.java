package eu.dzhw.fdz.metadatamanagement.studymanagement.service;

import java.io.IOException;
import java.lang.reflect.Field;
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

import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Search;
import lombok.extern.slf4j.Slf4j;

/**
 * Service to get released studies out of elastic search.
 * 
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
  public Page<Study> loadStudies(int page, int size) throws IOException {
    SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
    String[] fieldsAsString = geltFieldsFromStudy();

    sourceBuilder.fetchSource(fieldsAsString, null);
    sourceBuilder.query(QueryBuilders.boolQuery().filter(QueryBuilders.existsQuery("release")))
        .from(page * size).size(size).sort("title.de", SortOrder.ASC);
    Search search = new Search.Builder(sourceBuilder.toString()).addIndex("studies").build();
    JestResult searchResult = jestClient.execute(search);
    List<Study> hits = searchResult.getSourceAsObjectList(Study.class);
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
    Page<Study> resultPage = new PageImpl<Study>(hits, pageRequest, total);

    return resultPage;
  }

  /**
   * returns the array of fields from {@link Study} as Sting.
   * 
   * @return the fields
   */
  private String[] geltFieldsFromStudy() {
    Field[] asList = Study.class.getDeclaredFields();
    String[] fieldsAsString = new String[asList.length];
    for (int i = 0; i < asList.length; i++) {
      fieldsAsString[i] = asList[i].getName();
    }
    return fieldsAsString;
  }
}

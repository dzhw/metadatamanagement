package eu.dzhw.fdz.metadatamanagement.studymanagement.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.StudySearchDocument;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Search;

@Service
public class StudyListService {
  @Autowired
  JestClient jestClient;

  /**
   * Request released studies sort by title with a pagination defined by page and size.
   * 
   * @param page the pagination page
   * @param size the count of items to query
   * @return a list of searched study documents
   * @throws IOException if search failed
   */
  public List<StudySearchDocument> loadStudies(int page, int size) throws IOException {
    List<StudySearchDocument> result = new ArrayList<>();
    //TODO use query builder
    StringBuilder query = new StringBuilder("{").append("\"sort\":[\"_score\",\"id\"],")
        .append("\"query\":{\"bool\":{\"must\":[{\"match_all\":{}}],")
        .append("\"filter\":[{\"exists\":{\"field\":\"release\"}}]}},").append("\"from\":")
        .append(page).append(",\"size\":").append(size).append("}");

    Search request = new Search.Builder(query.toString()).build();
    JestResult searchResult = jestClient.execute(request);
    List<StudySearchDocument> hits = searchResult.getSourceAsObjectList(StudySearchDocument.class);
    result = hits;
    return result;
  }
}

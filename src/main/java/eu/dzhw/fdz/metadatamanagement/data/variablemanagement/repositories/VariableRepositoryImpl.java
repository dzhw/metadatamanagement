package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories;

import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;

/**
 * This class implements the interface of the custom variable documents repository. This class will
 * be used for the implementation of the repository beans.
 * 
 * @author Daniel Katzberg
 *
 */
public class VariableRepositoryImpl implements VariableRepositoryCustom {

  @Autowired
  private ElasticsearchTemplate elasticsearchTemplate;

  /*
   * (non-Javadoc)
   * 
   * @see
   * eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repository.VariablesRepositoryCustom#
   * searchAllFields(java.lang.String, org.springframework.data.domain.Pageable)
   */
  @Override
  public Page<VariableDocument> searchAllFields(String query, Pageable pageable) {
    SearchQuery searchQuery =
        new NativeSearchQueryBuilder()
            .withQuery(
                QueryBuilders.matchQuery("_all", query).fuzziness(Fuzziness.fromSimilarity(0.1f)))
            .withPageable(pageable).withHighlightFields(new HighlightBuilder.Field("name")).build();

    return this.elasticsearchTemplate.queryForPage(searchQuery, VariableDocument.class);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repository.VariablesRepositoryCustom#
   * searchPhrasePrefix(java.lang.String, org.springframework.data.domain.Pageable)
   */
  @Override
  public Page<VariableDocument> searchPhrasePrefix(String term, Pageable pageable) {

    QueryBuilder query = QueryBuilders.matchPhrasePrefixQuery("name", term);


    // QueryBuilder query =
    // QueryBuilders.matchPhraseQuery("name", term).fuzziness(Fuzziness.fromSimilarity(0.1f));

    SearchQuery searchQuery =
        new NativeSearchQueryBuilder().withQuery(query).withPageable(pageable)
            .withHighlightFields(new HighlightBuilder.Field("name")).build();

    return this.elasticsearchTemplate.queryForPage(searchQuery, VariableDocument.class);
    /*
     * Page<VariableDocument> results = elasticsearchTemplate.queryForPage(searchQuery,
     * VariableDocument.class, new SearchResultMapper() {
     * 
     * @Override public <T> FacetedPage<T> mapResults(SearchResponse response, Class<T> clazz,
     * Pageable pageable) { List<VariableDocument> chunk = new ArrayList<VariableDocument>(); for
     * (SearchHit searchHit : response.getHits()) { if (response.getHits().getHits().length <= 0) {
     * return null; } VariableDocument document = new VariableDocument();
     * document.setName(searchHit.getHighlightFields().get("name").fragments()[0] .toString());
     * chunk.add(document); } if (chunk.size() > 0) { return new FacetedPageImpl<T>((List<T>)
     * chunk); } return null; }
     * 
     * });
     * 
     * return results;
     */
  }
}

package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories.mapper;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.Aggregations;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.DefaultResultMapper;
import org.springframework.data.elasticsearch.core.FacetedPage;

/**
 * The aggregation result mapper is a sub class from the {@link DefaultResultMapper}. It extends the
 * MapResuts method for saving the aggregations.
 * 
 * @author Daniel Katzberg
 *
 */
public class AggregationResultMapper extends DefaultResultMapper {

  /**
   * The actual aggregations of a query result.
   */
  private Aggregations aggregations;

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.springframework.data.elasticsearch.core.DefaultResultMapper#mapResults(org.elasticsearch.
   * action.search.SearchResponse, java.lang.Class, org.springframework.data.domain.Pageable)
   */
  @Override
  public <T> FacetedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {

    this.aggregations = response.getAggregations();
    return super.mapResults(response, clazz, pageable);
  }

  /* GETTER / SETTER */
  public Aggregations getAggregations() {
    return aggregations;
  }
}
